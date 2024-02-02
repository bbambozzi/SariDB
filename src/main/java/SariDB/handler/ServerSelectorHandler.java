package SariDB.handler;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Set;

public record ServerSelectorHandler(Selector selector) implements Runnable {
    // private static final handled

    /**
     * Handles reading events from the Selector for socket channels in a non-blocking manner.
     * This method continuously monitors the Selector for readable channels and launches
     * a new thread for each valid key encountered, delegating the handling to the
     * {@link ClientSocketChannelHandler}.
     * <p>
     * Note: This method uses non-blocking I/O operations with a Selector and launches
     * threads for handling each channel. It cancels the keys after processing to avoid
     * reprocessing them in the next iteration.
     * </p>
     */
    private void handleReadingSelector() {
        try {
            while (true) {
                while (selector.selectNow() > 0) {
                    Set<SelectionKey> keys = selector.selectedKeys();
                    var iter = keys.iterator();
                    while (iter.hasNext()) {
                        var key = iter.next();
                        iter.remove();
                        if (key.isValid()) {
                            Thread.ofVirtual().start(new ClientSocketChannelHandler((SocketChannel) key.channel()));
                        }
                        key.cancel();
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Server selector handler failed");
        }
    }

    @Override
    public void run() {
        handleReadingSelector();
    }
}
