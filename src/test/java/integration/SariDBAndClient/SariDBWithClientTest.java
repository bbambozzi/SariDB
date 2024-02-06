package integration.SariDBAndClient;

import SariDB.db.SariDB;
import client.SariDBClient;
import org.junit.jupiter.api.*;

import java.net.Socket;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

public class SariDBWithClientTest {
    private static AtomicInteger count = new AtomicInteger(5759);
    @Nested
    @DisplayName("When a SariDB client connects to a standalone SariDB instance")
    public class setup {
        private static SariDBClient client;
        private static Socket socket;
        private static SariDB sariDB;

        @BeforeAll
        public static void setup() throws Exception {
            int portNumber = count.getAndAdd(1);
            sariDB = SariDB
                    .builder()
                    .isEmbedded(false)
                    .reconstruct(false)
                    .portNumber(portNumber)
                    .build();
            sariDB.start();
            Thread.sleep(2000); // Wait for server to start.
            socket = new Socket("localhost", portNumber);
            client = new SariDBClient(socket.getInputStream(), socket.getOutputStream());
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
