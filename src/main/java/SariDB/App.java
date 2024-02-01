package SariDB;

import SariDB.handler.ClientHandler;

/**
 * @author Bautista Bambozzi
 * @since 2023
 * @version 0.1
 */
public class App {
    public static void main(String[] args) {
        int portNumber = args.length >= 1 ? Integer.parseInt(args[0]) : 1338;
        ClientHandler clientHandler = new ClientHandler(portNumber);
        clientHandler.handleClients();
    }
}
