package jkv.handler;

import java.io.*;
import java.net.Socket;
import java.util.logging.Logger;

public record ConnectionHandler(Socket socket, BufferedReader reader, BufferedWriter writer) {
    private static final Logger logger = Logger.getLogger(ConnectionHandler.class.getName());

    public ConnectionHandler(Socket socket) throws IOException {
        this(
                socket,
                new BufferedReader(new InputStreamReader(socket.getInputStream())),
                new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()))
        );
    }

    public void start() {
        System.out.println("stuff..");
        while (reader().)
    }
}
