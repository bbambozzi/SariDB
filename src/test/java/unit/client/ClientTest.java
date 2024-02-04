package unit.client;

import client.SariDBClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;

import java.net.Socket;

public class ClientTest {
    @Nested
    public class ClientQueriesStandaloneSariDB {
        SariDBClient client;

        @BeforeEach
        public void setUp() throws Exception {
            try (Socket socket = new Socket("localhost", 1338)) {
                this.client = new SariDBClient(socket.getInputStream(), socket.getOutputStream());
            }
        }
    }
}
