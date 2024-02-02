package SariDB;

import SariDB.db.SariDB;

public class Standalone {
    public static void main(String[] args) {
        SariDB sariDB = SariDB
                .builder()
                .isEmbedded(true) // Embedded or standalone?
                .filePath(null) // Persistence file path?
                .reconstruct(false) // Reconstruct or start anew?
                .build();
        sariDB.start();
    }
}
