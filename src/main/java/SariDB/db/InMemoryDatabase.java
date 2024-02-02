package SariDB.db;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Represents the in-memory Key-Value database.
 * More functionality, like persistence, will be added in future releases.
 * <p>
 * NOTE: All of the methods in this class are thread-safe.
 * You should not try to create an instance of this class. Call the static methods instead.
 * A bug currently exists in the JDK whereas a Virtual {@link Thread} can pin and deadlock under specific circumstances.
 * We will continue to use them, awaiting the bugfix.
 * </p>
 */
public class InMemoryDatabase {

    private static final ConcurrentHashMap<String, String> inMemKVStore = new ConcurrentHashMap<>();
    private static final String nullResponse = "NULL";

    private InMemoryDatabase() {
    }

    /**
     * Thread-safe.
     * Clears the in-memory database.
     * Does not delete the SariFile.
     */
    public static void reset() {
        inMemKVStore.clear();
    }

    /**
     * Deletes the key-value pair associated with the specified key.
     *
     * @param key The key to be deleted.
     */
    public static void delete(String key) {
        inMemKVStore.remove(key);
    }

    /**
     * Retrieves the current size of the in-memory database.
     *
     * @return The number of key-value pairs in the database.
     */
    public static int size() {
        return inMemKVStore.size();
    }

    /**
     * Retrieves the value associated with the specified key.
     *
     * @param key The key for which to retrieve the value.
     * @return The value associated with the key, or "NULL" if the key is not found.
     */
    public static String get(String key) {
        return inMemKVStore.getOrDefault(key, nullResponse);
    }

    /**
     * Sets a key-value pair in the in-memory database.
     *
     * @param key   The key for the new entry.
     * @param value The value associated with the key.
     */
    public static void set(String key, String value) {
        inMemKVStore.put(key, value);
    }
}
