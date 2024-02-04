package unit.client;

import client.SariDBClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.net.Socket;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ClientTest {
    @Nested
    public class ClientQueriesStandaloneSariDB {
        SariDBClient client;
        private Socket socket;

        @BeforeEach
        public void setUp() throws Exception {
            this.socket = new Socket("localhost", 1338);
            this.client = new SariDBClient(socket.getInputStream(), socket.getOutputStream());
        }

        @Test
        public void shouldSetCorrectly() throws Exception {
            String answer = client.sendSetRequest("one", "one");
            assertEquals("OK", answer);
        }

        @Test
        public void shouldGetNullValuesCorrectly() throws Exception {
            String answer = client.sendGetRequest("IDONTEXIST");
            assertEquals("null", answer);
        }


        @Test
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
