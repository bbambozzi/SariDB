package SariDB;


import SariDB.db.SariDB;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class SariDBStandaloneTest {

    @Test
    public void standaloneSariDBIdentifiesAsSuch() {
        SariDB db = SariDB
                .builder()
                .isEmbedded(false)
                .build();
        assertFalse(db.isEmbedded());
    }

    @Test void SariDBInstanceIsCreatedWithBuilderCorrectly() {
        SariDB db = SariDB
                .builder()
                .isEmbedded(false)
                .build();
        assertFalse(db.isEmbedded());
        assertInstanceOf(SariDB.class, db);
    }
    @Test
}
