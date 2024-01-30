package jkv.handler;

import java.net.ServerSocket;
import java.net.Socket;
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
        try (ServerSocket sv = new ServerSocket(portNumber)) {
            logger.log(Level.INFO, "Server starting on port " + portNumber + "..!");
            while (!sv.isClosed()) {
                Socket socket = sv.accept();
                Thread.ofVirtual().start(new ConnectionHandler(socket));
            }
            logger.log(Level.SEVERE, "Server socket has been closed!");
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Server shutting down!");
            logger.log(Level.SEVERE, e.getMessage());
            // todo: log
        }
    }
}
