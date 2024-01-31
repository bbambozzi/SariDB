package jkv.handler;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Set;

public record ClientSocketChannelHandler(Selector selector) implements Runnable {

    private void handle() {
        try {
            while (!Thread.interrupted()) {
                while (selector.select() > 0) {
                    Set<SelectionKey> keys = selector.selectedKeys();
                    var iter = keys.iterator();
                    while (iter.hasNext()) {
                        var key = iter.next();
                        if (key.isReadable()) {
                            // TODO handle this channel!
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
        handle();
    }
}
