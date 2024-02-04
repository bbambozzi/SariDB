package SariDB.handler;

import SariDB.command.CommandHandler;

import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

public record ClientSocketChannelHandler(SocketChannel socketChannel) implements Runnable {
    private static final Logger logger = Logger.getLogger(ClientSocketChannelHandler.class.getName());

    /**
     * Handles incoming data from the client using non-blocking I/O with a Selector.
     * This method continuously monitors the Selector for readable events on the associated
     * {@code socketChannel}. It reads data into a ByteBuffer, launches a new thread for
     * processing the received data using the {@link CommandHandler}, and clears the buffer
     * for the next read. The method uses non-blocking I/O with a Selector to efficiently
     * handle multiple channels in a single thread.
     * <p>
     * Note: This method configures the {@code socketChannel} for non-blocking mode,
     * and it handles channel registration for both read and write events.
     * The method closes the channel if the end of the stream is reached or an exception occurs.
     * </p>
     */
    public void handleNewDataFromClient() {
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        try {
            if (socketChannel == null || !socketChannel.isOpen()) {
                return;
            }

            Selector selector = Selector.open();
            socketChannel.configureBlocking(false);
            socketChannel.register(selector, SelectionKey.OP_READ | SelectionKey.OP_WRITE);

            int bytesRead = 0;

            while (bytesRead >= 0) {
                selector.select(); // Wait for events (readable, writable, etc.)

                Set<SelectionKey> keys = selector.selectedKeys();
                Iterator<SelectionKey> iterator = keys.iterator();

                while (iterator.hasNext()) {
                    SelectionKey key = iterator.next();
                    iterator.remove();

                    if (key.isReadable()) {
                        bytesRead = socketChannel.read(buffer);

                        if (bytesRead > 0) {
                            buffer.flip(); // Reset position to zero
                            byte[] receivedBytes = new byte[buffer.remaining()];
                            buffer.get(receivedBytes);
                            Thread.ofVirtual().start(new CommandHandler(socketChannel, receivedBytes));
                            buffer.clear(); // Clear the buffer for the next read
                        }
                    }
                }
            }
            // If bytesRead is -1, the end of the stream is reached
            socketChannel.close();
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage());
        }
    }


    @Override
    public void run() {
        handleNewDataFromClient();
    }
}
