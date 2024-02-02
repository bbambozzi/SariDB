package SariDB.command;

import SariDB.command.util.Command;
import SariDB.command.util.Instruction;
import SariDB.db.InMemoryDatabase;

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

    /**
     * This method handles writing to the ClientExample socket. It will not do so if the ClientExample socket is closed or {@code null}.
     *
     * @param msg The string to be written to the ClientExample socket.
     * @since 2024
     */
    private void writeToSocket(String msg) {
        try {
            ByteBuffer buf = stringToResponseBuffer(msg);
            if (socketChannel == null || !socketChannel.isOpen()) {
                return;
            }
            socketChannel.write(buf);
        } catch (Exception e) {
            logger.log(Level.INFO, "Failed to write to ClientExample socket!");
        }
    }

    /**
     * @param command Must be a String representation of a {@link Command}.
     * @param fval    Must be a {@code String}, or in the case of a {@link Command} a valid string representation
     *                of an {@link Instruction}. Must never be {@code null}
     * @param sval    Sets the value to be stored for the case of a SET {@link Command}
     * @since 2024
     */
    private void handleUserCommand(String command, String fval, String sval) {
        if (command == null || fval == null) {
            return;
        }
        try {
            switch (Instruction.valueOf(command)) {
                case Instruction.SET -> {
                    if (sval == null) {
                        writeToSocket("ERR: MISSING ARGUMENT\n");
                        break;
                    }
                    InMemoryDatabase.set(fval, sval);
                    writeToSocket("OK\n");
                }
                case DEL -> {
                    writeToSocket("OK\n");
                }
                case GET -> {
                    writeToSocket(InMemoryDatabase.get(fval) + "\n");
                }
                case CMD -> {
                    switch (Command.valueOf(fval)) {
                        case RESET -> {
                            InMemoryDatabase.reset();
                            writeToSocket("OK\n");
                        }
                        case SIZE -> {
                            writeToSocket(InMemoryDatabase.size() + "\n");
                        }
                    }
                }
            }
        } catch (IllegalArgumentException ignored) {
            System.out.println("CMD=" + command + "FVAL= " + fval + " SVAL=" + sval);
            writeToSocket("ERR: Invalid Command. Try CMD HELP for help\n");
            logger.log(Level.INFO, "Invalid command received from the ClientExample");
        } catch (Exception e) {
            logger.log(Level.INFO, "Got " + command + " " + fval + " " + sval);
        }
    }

    @Override
    public void run() {
        handleReceivedBytes();
    }
}
