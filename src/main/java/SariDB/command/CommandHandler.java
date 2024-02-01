package SariDB.command;

import SariDB.db.InMemoryDatabase;
import SariDB.utils.Instruction;

import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.logging.Level;
import java.util.logging.Logger;

public record CommandHandler(SocketChannel socketChannel, byte[] receivedBytes) implements Runnable {
    private static final Logger logger = Logger.getLogger(CommandHandler.class.getName());
    private static final String splitByWhitespaceRegex = "\\s+";

    private static ByteBuffer stringToResponseBuffer(String string) {
        return ByteBuffer.wrap(string.getBytes());
    }

    public void handleReceivedBytes() {
        try {
            String[] receivedCommand = new String(receivedBytes).split(splitByWhitespaceRegex);
            int maxLen = receivedCommand.length;
            String cmd = maxLen >= 1 ? receivedCommand[0] : null;
            String fval = maxLen >= 2 ? receivedCommand[1] : null;
            String sval = maxLen >= 3 ? receivedCommand[2] : null;
            handleUserCommand(cmd, fval, sval);
        } catch (Exception ignored) {
        }

    }

    private void writeToSocket(String msg) {
        try {
            ByteBuffer buf = stringToResponseBuffer(msg);
            if (!socketChannel.isOpen()) {
                return;
            }
            socketChannel.write(buf);
        } catch (Exception e) {
            logger.log(Level.INFO, "Failed to write to client socket!");
        }
    }

    private void handleUserCommand(String command, String fval, String sval) {
        try {
            if (!socketChannel.isConnected()) {
                return;
            }

            switch (Instruction.valueOf(command)) {
                case SET -> {
                    if (fval == null || sval == null) {
                        writeToSocket("ERR: MISSING ARGUMENT\n");
                        System.out.println("Set received");
                        break;
                    }
                    InMemoryDatabase.set(fval, sval);
                    writeToSocket("OK\n");
                }
                case DEL -> {
                    if (fval == null) {
                        writeToSocket("ERR: MISSING ARGUMENT\n");
                    } else {
                        writeToSocket("OK\n");
                    }
                }
                case GET -> {
                    if (fval == null) {
                        writeToSocket("ERR: MISSING ARGUMENT\n");
                    }
                    writeToSocket(InMemoryDatabase.get(fval) + "\n");
                }
                case CMD -> {
                    switch (fval) {
                        case "RESET" -> {
                            InMemoryDatabase.reset();
                            writeToSocket("OK\n");
                        }
                        case "SIZE" -> {
                            writeToSocket(InMemoryDatabase.size() + "\n");
                        }
                    }
                }
            }
        } catch (Exception e) {
            logger.log(Level.INFO, "Got " + command + " " + fval + " " + sval);
        }
    }

    @Override
    public void run() {
        handleReceivedBytes();
    }
}
