package jkv.handler;

import java.net.Socket;

public record ConnectionHandler(Socket socket) {

    public static void start() {
        System.out.println("stuff.");
    }
}
