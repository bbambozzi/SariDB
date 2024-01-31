package SariDB.handler;

import SariDB.db.InMemoryDatabase;
import SariDB.utils.Instruction;

import java.io.*;
import java.net.Socket;
import java.util.Arrays;
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
        try {
            if (reader != null) reader.close();
            if (writer != null) writer.close();
            if (socket != null) socket.close();
        } catch (Exception ignored) {
        }
    }

    private void handleUserCommand(String command, String fval, String sval) {
        try {
            switch (Instruction.valueOf(command)) {
                case SET -> {
                    if (fval == null || sval == null) {
                        writer.write("ERR: MISSING ARGUMENT\n");
                        break;
                    }
                    InMemoryDatabase.set(fval, sval);
                    writer.write("OK");
                }
                case GET -> {
                    if (fval == null) {
                        writer.write("ERR: MISSING ARGUMENT\n");
                    }
                    writer.write(InMemoryDatabase.get(fval) + "\n");
                }
                case CMD -> {
                    switch (fval) {
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
            logger.log(Level.INFO, "Received " + command + " " + fval + " " + sval);
            writer.flush();
            System.out.println("Flushed!");
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
        if (socket == null || socket.isClosed() || !socket.isConnected()) {
            cleanup();
            return;
        }
        try {
            while (socket.isConnected() && !socket.isClosed() && reader != null) {
                if (reader.ready()) {
                    var line = reader.readLine();
                    String[] split = line.split("\\s+");
                    System.out.println("GOT " + Arrays.toString(split));
                    int maxLen = split.length;
                    String cmd = maxLen >= 1 ? split[0] : null;
                    String fval = maxLen >= 2 ? split[1] : null;
                    String sval = maxLen >= 3 ? split[2] : null;
                    handleUserCommand(cmd, fval, sval);
                }
                Thread.sleep(50); // todo refactor?
            }
        } catch (Exception e) {
            logger.log(Level.INFO, "failed to readline from client socket! cleaning up..");
        } finally {
            cleanup();
        }
    }
}
