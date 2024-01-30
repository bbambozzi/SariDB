package jkv.db;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Bautista Bambozzi
 * @since 2024
 */
public record InMemoryDatabase() {

    private static final ConcurrentHashMap<String, String> inMemKVStore = new ConcurrentHashMap<>();
    private static final String nullResponse = "NULL";

    public static String get(String val) {
        return inMemKVStore.getOrDefault(val, nullResponse);
    }

    public static void set(String key, String value) {
        inMemKVStore.put(key, value);
    }
}
