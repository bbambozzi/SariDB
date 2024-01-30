package jkv.handler;

import jkv.utils.Instruction;

import java.io.*;
import java.net.Socket;
import java.util.Arrays;
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

    private void handleUserCommand(String command) {
        try {
            switch(Instruction.valueOf(command)) {
                case SET -> {
                    System.out.println("Got a valid instruction: SET");
                    writer.write("Got a valid instruction: SET");
                }
                case GET -> {
                    System.out.println("Got a valid instruction: GET");
                    writer.write("Got a valid instruction: CMD");
                }
                case CMD -> {
                    System.out.println("Got a valid instruction: CMD");
                    writer.write("Got a valid instruction: CMD");
                }
            }

        } catch (Exception e) {
            try {
                writer.write("Invalid command received! got=" + command);
            } catch (IOException ex) {
                logger.log(Level.SEVERE, "Failed to write to client socket!");
            }
        }
    }




    public void start() throws IOException {
        logger.log(Level.INFO, "new client connected! client=" + socket.toString());
        try {
            while (socket.isConnected() && !socket.isClosed()) {
                if (reader.ready()) {
                    var line = reader.readLine(); // todo optimize maybe?
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
