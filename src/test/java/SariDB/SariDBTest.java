package SariDB;


import SariDB.db.SariDB;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit test for simple App.
 */
public class SariDBTest
{
    /**
     * Rigorous Test :-)
     */
    @Test
    public void contextBuilds()
    {
        assertTrue(true);
    }

    @Test
    public void SariDBBuildsCorrectly() {
        SariDB db = SariDB.builder()
                .build();
        assertInstanceOf(SariDB.class, db);
    }

    @Test
    public void embeddedSariWorks() {
        SariDB db = SariDB.builder()
                .isEmbedded(true)
                .filePath("here.db")
                .reconstruct(false)
                .build();

        assertNotNull(db);
        String k = "one";
        String v = "two";
        db.set(k, v);

        assertNotNull(db.get(k));

    }


}
