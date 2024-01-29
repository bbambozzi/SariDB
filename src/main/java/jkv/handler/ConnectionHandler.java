package jkv.handler;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public record ConnectionHandler(Socket socket, BufferedInputStream inputStream) {
    private static final Logger logger = Logger.getLogger(ConnectionHandler.class.getName());

    public ConnectionHandler(Socket socket) throws IOException {
            this(socket, new BufferedInputStream(socket.getInputStream()));
    }

    public void start() {
        System.out.println("stuff..");

    }
}
