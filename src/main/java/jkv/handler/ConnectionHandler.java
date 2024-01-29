package jkv.handler;

import java.io.*;
import java.net.Socket;
import java.util.logging.Level;
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

    public void cleanup() throws IOException{
        if (socket != null) socket.close();
        if (reader != null) reader.close();
        if (writer != null) writer.close();
    }


    public void start() throws IOException {
        logger.log(Level.INFO, "new client connected! client=" + socket.toString());
        while (socket.isConnected() && !socket.isClosed()) {
            try {
                if (reader.ready()) {
                    reader.lines()
                            .forEach(System.out::println);
                }
            } catch (IOException e) {
                cleanup();
            }
        }
    }
}
