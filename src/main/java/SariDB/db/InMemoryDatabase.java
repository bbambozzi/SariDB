package SariDB.db;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Represents the in-memory Key-Value database.
 * More functionality, like persistence, will be added in future releases.
 * <p>
 * NOTE: All of the methods in this class are thread-safe.
 * Do not try to create an instance of this class; call the static methods instead.
 * </p>
 */
public class InMemoryDatabase {

    private static final ConcurrentHashMap<String, String> inMemKVStore = new ConcurrentHashMap<>();
    private static final String nullResponse = "null";

    /**
     * Private method to prevent instantiation
     */
    private InMemoryDatabase() {
    }


    public static ConcurrentHashMap<String, String> cloneInMemKV() {
        return new ConcurrentHashMap<>(inMemKVStore);
    }

    /**
     * Thread-safe.
     * Clears the in-memory database.
     * Does not delete the persistence file.
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
