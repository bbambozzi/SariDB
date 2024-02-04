package SariDB;


import SariDB.db.SariDB;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

public class SariDBStandaloneTest {

    @Test
    @DisplayName("SariDB standalone correctly identifies as standalone.")
    public void standaloneSariDBIdentifiesAsSuch() {
        SariDB db = SariDB
                .builder()
                .isEmbedded(false)
                .build();
        assertFalse(db.isEmbedded());
    }

    @DisplayName("SariDB instance is of the correct type after using the builder")
    @Test
    void SariDBInstanceIsCreatedWithBuilderCorrectly() {
        SariDB db = SariDB
                .builder()
                .isEmbedded(false)
                .build();
        assertFalse(db.isEmbedded());
        assertInstanceOf(SariDB.class, db);


    }
    @Nested
    public class SariDBStandaloneInteractions {
        private SariDB db;

        @BeforeEach
        public void init() {
            db = SariDB
                    .builder()
                    .isEmbedded(false)
                    .reconstruct(false)
                    .build();
        }
    }
}
