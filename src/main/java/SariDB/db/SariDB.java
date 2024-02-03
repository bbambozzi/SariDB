package SariDB.db;

import SariDB.handler.ClientHandler;
import SariDB.handler.ServerSelectorHandler;
import org.apache.hadoop.fs.Path;

import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Bautista Bambozzi
 * @version 0.1
 * @see SariDB.Builder
 * @since 2024
 * <h3>A lightweight, concurrent key-value database that can be embedded or deployed standalone.</h3>
 * <p>Can be instantiated via the {@code SariDB.builder()} method.</p>
 * Represents a SariDB instance that can be configured using the Builder pattern.
 * This class allows creating a SariDB object with specified properties such as
 * the file path and whether it is embedded.
 */
public class SariDB {
    private final boolean isEmbedded;
    private final PersistenceHandler persistenceHandler;
    private final Logger logger = Logger.getLogger(SariDB.class.getName());
    private final boolean reconstruct;
    private final int portNumber;

    private void rebuild() {
        if (reconstruct) {
            ConcurrentHashMap<String, String> newDb = new ConcurrentHashMap<>(); // TODO
            InMemoryDatabase.swapFor(newDb);
        }
    }


    private SariDB(Builder builder) {
        this.isEmbedded = builder.isEmbedded;
        this.reconstruct = builder.reconstruct;
        this.portNumber = builder.portNumber;
        this.persistenceHandler = new PersistenceHandler(new Path(builder.filePath));
    }

    /**
     * Creates a new instance of the SariDB Builder.
     *
     * @return A new instance of the SariDB Builder.
     */
    public static Builder builder() {
        return new Builder()
                .isEmbedded(true)
                .reconstruct(false)
                .portNumber(1338)
                .filePath("db.parquet");
    }

    public final void start() {
        logger.log(Level.INFO, "Starting SariDB " + (isEmbedded ? " in embedded mode" : " in standalone mode"));
        if (reconstruct) {
            rebuild();
        }
        if (!isEmbedded) {
            Thread.ofVirtual().start(
                    new ClientHandler(portNumber)
            );
        }
    }

    public String get(String key) {
        String k = Objects.requireNonNullElseGet(key, () -> "null");
        return InMemoryDatabase.get(k);
    }

    public final void set(String key, String value) {
        String k = Objects.requireNonNullElseGet(key, () -> "null");
        String v = Objects.requireNonNullElseGet(value, () -> "null");
        InMemoryDatabase.set(k, v);
    }

    public final void delete(String key) {
        String k = Objects.requireNonNullElseGet(key, () -> "null");
        InMemoryDatabase.delete(k);
    }

    public final void save() {
        try {
            this.persistenceHandler.writeToFile(InMemoryDatabase.cloneInMemKV());
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Failed to save persistence file!");
        }
    }

    /**
     * Builder class for configuring and creating a SariDB instance.
     */
    public static class Builder {
        private boolean reconstruct;
        private boolean isEmbedded;
        private String filePath;
        private int portNumber;

        /**
         * Builds and returns the SariDB instance with the configured properties.
         *
         * @return The configured SariDB instance.
         */
        public SariDB build() {
            return new SariDB(this);
        }

        public Builder portNumber(int portNumber) {
            this.portNumber = portNumber;
            return this;
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

        /**
         * Sets whether the SariDB instance being built should reconstruct itself
         *
         * @param reconstruct True if the SariDB should reconstruct itself, false otherwise.
         * @return The Builder instance for method chaining.
         */
        public Builder reconstruct(boolean reconstruct) {
            this.reconstruct = reconstruct;
            return this;
        }
    }
}