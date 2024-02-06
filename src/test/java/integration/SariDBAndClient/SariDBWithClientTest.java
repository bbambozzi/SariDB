package integration.SariDBAndClient;

import SariDB.db.SariDB;
import client.SariDBClient;
import org.junit.jupiter.api.*;

import java.io.IOException;
import java.net.Socket;

import static org.junit.jupiter.api.Assertions.*;

public class SariDBWithClientTest {
    @Nested
    @DisplayName("When a SariDB client connects to a standalone SariDB instance")
    public class ClientQueriesStandaloneSariDB {
        SariDBClient client;
        private Socket socket;
        private SariDB sariDB;

        ClientQueriesStandaloneSariDB() throws IOException {
            sariDB = SariDB
                    .builder()
                    .isEmbedded(false)
                    .reconstruct(false)
                    .portNumber(5789)
                    .build();
            sariDB.start();
            this.socket = new Socket("localhost", 5789);
            this.client = new SariDBClient(socket.getInputStream(), socket.getOutputStream());
        }

        @Test
        @DisplayName("SET commands are registered correctly")
        public void shouldSetCorrectly() throws Exception {
            assertFalse(sariDB.isEmbedded());
            String answer = client.sendSetRequest("one", "one");
            assertEquals("OK", answer);
        }

        @DisplayName("Non-existing values return 'null'")
        @Test
        public void shouldGetNullValuesCorrectly() throws Exception {
            String answer = client.sendGetRequest("IDONTEXIST");
            assertEquals("null", answer);
        }


        @Test
        @DisplayName("Setting and then getting a value will yield the correct result")
        public void shouldSetAndGetValuesCorrectly() throws Exception {
            String setValue = "testingValue";
            String setKey = "testingkey";
            String okResponse = client.sendSetRequest(setKey, setValue);
            String getResponse = client.sendGetRequest(setKey);
            assertEquals("OK", okResponse);
            assertEquals(getResponse, setValue);
        }
    }
}
