package jkv;

import jkv.handler.ClientHandler;

/**
 * @author Bautista Bambozzi
 * @since 2023
 * @version 0.1
 */
public class App {
    public static void main(String[] args) {
        ClientHandler clientHandler = new ClientHandler(1338);
        clientHandler.handleClients();
    }
}
