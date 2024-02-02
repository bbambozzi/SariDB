package SariDB.db;

import SariDB.handler.ClientHandler;

/**
 * @author Bautista Bambozzi
 * @version 0.1
 * @since 2023
 */
public class SariDB {
    private final boolean isEmbedded;
    private final String filePath;

    private SariDB(Builder builder) {
        this.filePath = builder.filePath;
        this.isEmbedded = builder.isEmbedded;
    }

     static void main(String[] args) {
        int portNumber = args.length >= 1 ? Integer.parseInt(args[0]) : 1338;
        ClientHandler clientHandler = new ClientHandler(portNumber);
        clientHandler.handleClients();
    }

    public static class Builder {
        private boolean isEmbedded;
        private String filePath;

        public SariDB build() {
            return new SariDB(this);
        }

        public Builder filePath(String filePath) {
            this.filePath = filePath;
            return this;
        }

        public Builder isEmbedded(boolean isEmbedded) {
            this.isEmbedded = isEmbedded;
            return this;
        }
    }
}
