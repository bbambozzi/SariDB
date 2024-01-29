package jkv.handler;

import java.net.ServerSocket;
import java.util.logging.Logger;

/**
 * ClientHandler
 */
public class ClientHandler {

  private static final Logger logger = Logger.getLogger(ClientHandler.class.getName());

  public static void handleClient() throws Exception {
    try (ServerSocket sv = new ServerSocket(1337)) {
      sv.accept();
    } catch (Exception e) {
      // todo: log
    }
  }
}
