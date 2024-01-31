package jkv.handler;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Set;
import java.util.concurrent.ConcurrentMap;

public record ServerSelectorHandler(Selector selector) implements Runnable {
    // private static final handled

    private void handleReadingSelector() {
        try {
            while (true) {
                while (selector.selectNow() > 0) {// this is blocking!
                    Set<SelectionKey> keys = selector.selectedKeys();
                    var iter = keys.iterator();
                    while (iter.hasNext()) {
                        System.out.println("Selector iterator has values!");
                        var key = iter.next();
                        iter.remove();
                        if (key.isReadable()) {
                            Thread.ofVirtual().start(new ClientSocketChannelHandler((SocketChannel) key.channel()));
                            System.out.println("Started a client socket channel handler");
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
