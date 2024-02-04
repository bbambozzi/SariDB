package SariDB;


import SariDB.db.SariDB;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit test for simple App.
 */
public class SariDBTest {
    /**
     * Rigorous Test :-)
     */
    @Test
    public void contextBuilds() {
        assertTrue(true);
    }

    @Test
    @DisplayName("SariDB is correctly created by using the builder")
    public void SariDBBuildsCorrectly() {
        SariDB db = SariDB
                .builder()
                .build();
        assertInstanceOf(SariDB.class, db);
    }

    @Test
    @DisplayName("Embedded SariDB identifies itself as such")
    public void embeddedSariWorks() {
        SariDB db = SariDB.builder()
                .isEmbedded(true)
                .filePath("here.db")
                .reconstruct(false)
                .build();

        assertNotNull(db);
        assertTrue(db.isEmbedded());

    }

    @Nested
    @DisplayName("When using SariDB on embedded mode")
    class SariDBInteractions {
        SariDB sariDB;

        @BeforeEach
        public void setUp() {
            this.sariDB = SariDB
                    .builder()
                    .isEmbedded(true)
                    .build();
        }

        @Test
        @DisplayName("Identifies as embedded")
        public void identifiesAsEmbedded() {
            assertTrue(this.sariDB.isEmbedded());
        }

        @Test
        @DisplayName("Should SET and GET values correctly")
        public void setAndGetValues() {
            String k = "arbitraryKey";
            String v = "arbitraryValue";
            sariDB.set(k, v);
            sariDB.get(v);

            assertEquals(v, sariDB.get(k));
            assertNotEquals(sariDB.get(v), v);
        }

        @Test
        @DisplayName("Should correctly delete a key")
        public void shouldDeleteKey() {
            String k = "arbitraryKey";
            String v = "arbitraryValue";
            sariDB.set(k, v);
            sariDB.get(v);
            assertEquals(sariDB.get("key"), "null");
        }
    }


}
