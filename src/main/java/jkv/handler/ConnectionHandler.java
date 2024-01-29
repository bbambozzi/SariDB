package jkv.handler;

import jkv.utils.Instruction;

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

    private final void cleanup() throws IOException {
        System.out.println("Started cleanup of client!");
        if (socket != null) socket.close();
        if (reader != null) reader.close();
        if (writer != null) writer.close();
    }

    private final void handleUserCommand(String command) {
        switch (command) {
        }
    }




    public void start() throws IOException {
        logger.log(Level.INFO, "new client connected! client=" + socket.toString());
        try {
            while (socket.isConnected() && !socket.isClosed()) {
                if (reader.ready()) {
                    String line = reader.readLine();
                    handleUserCommand(line);
                }
            }
        } catch (Exception e) {
            logger.log(Level.INFO, "failed to readline from client socket! cleaning up..");
        } finally {
            cleanup();
        }
    }
}
