package jkv.handler;

import jkv.db.InMemoryDatabase;
import jkv.utils.Instruction;

import java.io.*;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public record ConnectionHandler(Socket socket, BufferedReader reader, BufferedWriter writer) implements Runnable {
    private static final Logger logger = Logger.getLogger(ConnectionHandler.class.getName());

    public ConnectionHandler(Socket socket) throws IOException {
        this(
                socket,
                new BufferedReader(new InputStreamReader(socket.getInputStream())),
                new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()))
        );
    }

    private void cleanup() {
        System.out.println("Started cleanup of client!");
        try {
            if (socket != null && !socket.isClosed()) socket.close();
            if (reader != null) reader.close();
            if (writer != null) writer.close();
        } catch (Exception ignored) {
        }
    }

    private void handleUserCommand(String command, String fval, String sval) {
        try {
            switch (Instruction.valueOf(command)) {
                case SET -> {
                    writer.write("Got a valid instruction: SET\n");
                    if (fval == null || sval == null) {
                        writer.write("ERR: MISSING ARGUMENT");
                        break;
                    }
                    InMemoryDatabase.set(fval, sval);
                }
                case GET -> {
                    writer.write("Got a valid instruction: GET\n");
                    if (fval == null) {
                        writer.write("ERR: MISSING ARGUMENT");
                    }
                    writer.write(InMemoryDatabase.get(fval) + "\n");
                }
                case CMD -> {
                    writer.write("Got a valid instruction: CMD\n");
                    switch(fval) {
                        case "RESET" -> {
                            InMemoryDatabase.reset();
                            writer.write("RESET OK\n");
                        }
                        case "SIZE" -> {
                            writer.write(InMemoryDatabase.size() + "\n");
                        }
                    }
                }
            }
            writer.flush();
        } catch (Exception e) {
            try {
                writer.write("Invalid command received! got=" + command);
                writer.flush();
            } catch (IOException ex) {
                logger.log(Level.SEVERE, "Failed to write to client socket!");
            }
        }
    }


    @Override
    public void run() {
        logger.log(Level.INFO, "new client connected! client=" + socket.toString());
        try {
            while (socket.isConnected() && !socket.isClosed()) {
                if (reader.ready()) {
                    var line = reader.readLine(); // todo optimize maybe?
                    String[] split = line.split("\\s+");
                    int maxLen = split.length;
                    String cmd = maxLen >= 1 ? split[0] : null;
                    String fval = maxLen >= 2 ? split[1] : null;
                    String sval = maxLen >= 3 ? split[2] : null;
                    handleUserCommand(cmd, fval, sval);
                }
            }
        } catch (Exception e) {
            logger.log(Level.INFO, "failed to readline from client socket! cleaning up..");
        } finally {
            cleanup();
        }
    }
}
