package unit.SariDB;


import SariDB.db.SariDB;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit test for simple App.
 */
public class SariDBEmbeddedTest {
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
    @DisplayName("When using SariDB on embedded mode without reconstruction")
    class SariDBEmbeddedInteractions {
        SariDB sariDB;

        @BeforeEach
        public void setUp() {
            this.sariDB = SariDB
                    .builder()
                    .isEmbedded(true)
                    .reconstruct(false)
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
            assertEquals(sariDB.get(k), v);
            sariDB.delete(k);
            assertEquals(sariDB.get(k), "null");
        }
    }

    @Nested
    @DisplayName("When using SariDB on embedded mode and reconstruction enabled")
    public class SariDBReconstruction {
        SariDB sariDB;

        @BeforeEach
        @DisplayName("Should set up correctly")
        public void setUp() {
            this.sariDB = SariDB
                    .builder()
                    .filePath("src/test/resources/testing.parquet")
                    .isEmbedded(true)
                    .reconstruct(true)
                    .build();
        }

        @Test
        @DisplayName("Reads already-stored values after reconstruction correctly")
        public void readsValuesCorrectly() {
            String val = sariDB.get("5");
            System.out.println("getting saridb stuff");
            assertEquals("FIVE", val); // This value is hardcoded in the testing.parquet file.
        }

        @Test
        @DisplayName("Returns null on non-existing values after reconstruction")
        public void returnNullOnNonExistingValuesAfterReconstruction() {
            String val = sariDB.get("IDONTEXIST123");
            assertEquals(val, "null");
        }

        @Test
        @DisplayName("Can still change records that have previously been reconstructed from disk")
        public void canStillChangeRecordsFromMemoryAfterReconstruction() {
            sariDB.set("5", "NOTFIVE");
            assertEquals(sariDB.get("5"), "NOTFIVE");
        }

        @Test
        @DisplayName("Can still delete records that have been previously reconstructed from disk")
        public void canStillRemoveRecordsFromMemoryAfterReconstruction() {
            sariDB.delete("5");
            assertEquals(sariDB.get("5"), "null");
        }

        @Test
        @DisplayName("Correctly identifies itself as embedded after reconstruction")
        public void continuesToBeEmbeddedAfterReconstruction() {
            assertTrue(sariDB.isEmbedded());
        }
    }
}
