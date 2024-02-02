package SariDB.db;

import SariDB.handler.ClientHandler;

/**
 * @author Bautista Bambozzi
 * @version 0.1
 * @since 2024
 * <h3>A lightweight, concurrent key-value database that can be embedded or deployed standalone.</h3>
 * <p>Can be instantiated via the {@code SariDB.builder()} method.</p>
 * Represents a SariDB instance that can be configured using the Builder pattern.
 * This class allows creating a SariDB object with specified properties such as
 * the file path and whether it is embedded.
 * @see SariDB.Builder
 */
public class SariDB {
    private final boolean isEmbedded;
    private final String filePath;


    public final void start() {
        System.out.println("Starting ..!");
    }

    private SariDB(Builder builder) {
        this.filePath = builder.filePath;
        this.isEmbedded = builder.isEmbedded;
    }

    /**
     * Creates a new instance of the SariDB Builder.
     *
     * @return A new instance of the SariDB Builder.
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Builder class for configuring and creating a SariDB instance.
     */
    public static class Builder {
        private boolean reconstruct;
        private boolean isEmbedded;
        private String filePath;

        /**
         * Builds and returns the SariDB instance with the configured properties.
         *
         * @return The configured SariDB instance.
         */
        public SariDB build() {
            return new SariDB(this);
        }

        /**
         * Sets the file path for the SariDB instance being built.
         *
         * @param filePath The file path to set.
         * @return The Builder instance for method chaining.
         */
        public Builder filePath(String filePath) {
            this.filePath = filePath;
            return this;
        }

        /**
         * Sets whether the SariDB instance being built is embedded.
         *
         * @param isEmbedded True if the SariDB is embedded, false otherwise.
         * @return The Builder instance for method chaining.
         */
        public Builder isEmbedded(boolean isEmbedded) {
            this.isEmbedded = isEmbedded;
            return this;
        }

        public Builder reconstruct(boolean reconstruct) {
            this.reconstruct = reconstruct;
            return this;
        }
    }
}