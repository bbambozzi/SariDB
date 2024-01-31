package jkv.handler;

import java.io.IOException;
import java.nio.channels.*;
import java.util.Set;

public record ServerSelectorHandler(Selector selector) implements Runnable {

    private void handleReadingSelector() {
        try {
            while (!Thread.interrupted()) {
                while (selector.select() > 0) {
                    Set<SelectionKey> keys = selector.selectedKeys();
                    var iter = keys.iterator();
                    while (iter.hasNext()) {
                        var key = iter.next();
                        if (key.isReadable()) {
                            var th = Thread.ofVirtual();
                            th.start(new ClientSocketChannelHandler((SocketChannel) key.channel()));
                        }
                        iter.remove();
                    }
                }

            }
        } catch (IOException e) {
            // TODO: log
        }
    }

    @Override
    public void run() {
        handleReadingSelector();
    }
}
