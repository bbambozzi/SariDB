package SariDB.handler;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Set;

public record ServerSelectorHandler(Selector selector) implements Runnable {
    // private static final handled

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
