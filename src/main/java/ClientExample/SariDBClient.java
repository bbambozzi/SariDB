package ClientExample;

import com.jcraft.jsch.IO;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

public record SariDBClient(InputStream inputStream, OutputStream outputStream) {
    public static final Logger logger = Logger.getLogger(SariDBClient.class.getName());

    public String sendSetRequest(String key, String value) throws IOException {
            outputStream.write(("SET " + key + " " + value).getBytes());
            outputStream.flush();
            byte[] responseBuffer = new byte[1024];
            inputStream.read(responseBuffer);
            return new String(responseBuffer);
    }

    public String sendGetRequest(String valueToGet) throws IOException {
        outputStream.write(("GET " + valueToGet).getBytes());
        outputStream.flush();
        byte[] responseBuffer = new byte[1024];
        inputStream.read(responseBuffer);
        return new String(responseBuffer).trim();
    }

    public String sendDelRequest(String valueToDel) throws IOException {
        outputStream.write(("DEL " + valueToDel).getBytes());
        outputStream.flush();
        byte[] responseBuffer = new byte[1024];
        inputStream.read(responseBuffer);
        return new String(responseBuffer).trim();
    }
}