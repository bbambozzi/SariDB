package ClientExample;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

public record SariDBClient(InputStream inputStream, OutputStream outputStream) {
    public static final Logger logger = Logger.getLogger(SariDBClient.class.getName());

    public String sendSetRequest(String valueToSend) throws IOException {
            outputStream.write(("SET " + valueToSend).getBytes());
            outputStream.flush();
            byte[] responseBuffer = new byte[1024];
            inputStream.read(responseBuffer);
            return new String(responseBuffer);
    }

}
