package unit.client;

import client.SariDBClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.net.Socket;

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
            String x = client.sendSetRequest("one", "one");
            assertEquals(x, "OK");
        }

        @Test
        public void shouldGetNullValuesCorrectly() throws Exception {
            String x = client.sendGetRequest("IDONTEXIST");
            assertEquals(x, "null");
        }
    }
}
