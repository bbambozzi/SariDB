package SariDB;

import SariDB.db.SariDB;

public class Standalone {
    public static void main(String[] args) {
        SariDB sariDB = SariDB
                .builder()
                .isEmbedded(true) // Embedded or standalone?
                .filePath("here.parquet") // Save your .parquet wherever!
                .reconstruct(false) // Reconstruct or start anew?
                .build(); // That's all folks.
        sariDB.start(); // 🚀
    }
}
