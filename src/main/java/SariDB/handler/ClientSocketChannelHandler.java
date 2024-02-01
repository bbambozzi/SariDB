package SariDB.handler;

import SariDB.command.CommandHandler;

import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.logging.Logger;

public record ClientSocketChannelHandler(SocketChannel socketChannel) implements Runnable {
    private static final Logger logger = Logger.getLogger(ClientSocketChannelHandler.class.getName());

    public void handleNewDataFromClient() {
        try {
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            if (socketChannel == null || !socketChannel.isOpen()) {
                return;
            }
            int bytesRead = socketChannel.read(buffer);

            if (bytesRead == -1) {
                socketChannel.close();
            } else if (bytesRead > 0) {
                buffer.flip();
                byte[] receivedBytes = new byte[buffer.remaining()];
                buffer.get(receivedBytes);
                Thread.ofVirtual().start(new CommandHandler(socketChannel, receivedBytes));
            }
        } catch (Exception e) {
        }
    }


    @Override
    public void run() {
        handleNewDataFromClient();
    }
}
