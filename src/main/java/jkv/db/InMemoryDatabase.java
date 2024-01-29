package jkv.db;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Bautista Bambozzi
 * @since 2024
 */
public record InMemoryDatabase() {

    private static final ConcurrentHashMap<byte[], byte[]> inMemKVStore = new ConcurrentHashMap<>();
    private static final byte[] nullResponse = "NULL".getBytes();

    public static byte[] get(byte[] val) {
        return inMemKVStore.getOrDefault(val, nullResponse);
    }

    public static void set(byte[] key, byte[] value) {
        inMemKVStore.put(key, value);
    }
}
