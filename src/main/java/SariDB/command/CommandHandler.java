package SariDB.command;

import SariDB.command.util.CMDCommand;
import SariDB.command.util.Instruction;
import SariDB.db.InMemoryDatabase;
import SariDB.db.PersistenceHandler;
import org.apache.hadoop.fs.Path;

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
     * This method handles writing to the client socket. It will not do so if the client socket is closed or {@code null}.
     *
     * @param msg The string to be written to the client socket.
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
            logger.log(Level.INFO, "Failed to write to client socket!");
        }
    }

    /**
     * @param command Must be a String representation of a {@link CMDCommand}.
     * @param fval    Must be a {@code String}, or in the case of a {@link CMDCommand} a valid string representation
     *                of an {@link Instruction}. Must never be {@code null}
     * @param sval    Sets the value to be stored for the case of a SET {@link CMDCommand}
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
                    switch (CMDCommand.valueOf(fval)) {
                        case RESET -> {
                            InMemoryDatabase.reset();
                            writeToSocket("OK\n");
                        }
                        case SIZE -> {
                            writeToSocket(InMemoryDatabase.size() + "\n");
                        }
                        case SAVE -> {
                            if (sval == null) {
                                throw new IllegalArgumentException("Expected a valid path");
                            }
                            var persistenceHandler = new PersistenceHandler(new Path(sval + ".parquet"));
                            persistenceHandler.writeToFile(InMemoryDatabase.cloneInMemKV());
                            writeToSocket("OK\n");
                        }
                        case HELP -> {
                            writeToSocket("""
                                    SET (key) (value) Sets key and value to the in-memory database.
                                    GET (key) gets the key from the in-memory database. Returns "null" on non-existent key
                                    DEL (key) deletes the key from the in-memory database.
                                    
                                    CMD (COMMAND) runs the COMMAND specified.
                                    
                                    The available COMMAND are:
                                        - SAVE (filename) saves the in-memory database to the specified file.
                                            The persistence handler appends .parquet to the end of the filename.
                                        - SIZE returns the amount of key-value pairs in the in-memory database.
                                        - RESET removes all key-value pairs from the in-memory database.
                                    """);
                        }
                    }
                }
            }
        } catch (IllegalArgumentException ignored) {
            logger.log(Level.INFO, "Invalid command received. CMD=" + command + "FVAL= " + fval + " SVAL=" + sval);
            writeToSocket("ERR: Invalid Command. Try CMD HELP for help\n");
        } catch (Exception ignored) {
        }
    }

    @Override
    public void run() {
        handleReceivedBytes();
    }
}
