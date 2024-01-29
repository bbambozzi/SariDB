package jkv.handler;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Logger;

/**
 * ClientHandler
 * @author Bautista Bambozzi
 * @since 2024
 * @param portNumber The port number where the server will listen for connections.
 */
public record ClientHandler(int portNumber) {
  private static final Logger logger = Logger.getLogger(ClientHandler.class.getName());

  public void handleClients() throws Exception {
    try (ServerSocket sv = new ServerSocket(portNumber)) {
      logger.info("Server starting on port " + portNumber + "..!");
      while (true) { // TODO improve this
        Socket socket = sv.accept();
        new ConnectionHandler(socket).start();
      }
    } catch (Exception e) {
      // todo: log
    }
  }
}
