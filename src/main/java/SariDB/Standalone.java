package SariDB;

import SariDB.db.SariDB;

public class Standalone {
    public static void main(String[] args) throws InterruptedException {
        SariDB sariDB = SariDB
                .builder()
                .isEmbedded(false) // Embedded or standalone?
                .filePath("src/test/resources/testing.parquet") // Save your .parquet wherever!
                .reconstruct(true) // Reconstruct or start anew?
                .build(); // That's all folks.
        sariDB.start(); // 🚀
        while (!sariDB.isEmbedded()) {
            System.out.println(sariDB.get("5"));
            Thread.sleep(3000);
        }
    }
}
