package SariDB.handler;

import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * ClientHandler
 *
 * @param portNumber The port number where the server will listen for connections.
 * @author Bautista Bambozzi
 * @since 2024
 */
public record ClientHandler(int portNumber) {
    private static final Logger logger = Logger.getLogger(ClientHandler.class.getName());

    public void handleClients() {
        try (ServerSocketChannel svSocketChannel = ServerSocketChannel.open();
             Selector selector = Selector.open();
             ServerSocket sv = svSocketChannel.socket();
        ) {
            sv.bind(new InetSocketAddress(1338));
            svSocketChannel.configureBlocking(false);
            logger.log(Level.INFO, "SariDB Server starting on port " + portNumber + "..!");
            Thread.ofVirtual().start(new ServerSelectorHandler(selector)); // start the selector listener
            while (!sv.isClosed()) {
                SocketChannel socketChannel = svSocketChannel.accept();
                if (socketChannel != null) {
                    socketChannel.configureBlocking(false);
                    socketChannel.register(selector, SelectionKey.OP_READ | SelectionKey.OP_WRITE); // bitwise shift to accept both
                }
            }
            logger.log(Level.SEVERE, "SariDB Server socket has been closed!");
        } catch (Exception e) {
            logger.log(Level.SEVERE, "SariDB Server shutting down!");
            logger.log(Level.SEVERE, e.getMessage());
        }
    }
}
