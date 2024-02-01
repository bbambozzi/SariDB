package client;

import java.io.OutputStream;
import java.net.Socket;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;

public record ClientExample() {
    private static final Random random = new Random();
    private static final Logger logger = Logger.getLogger(ClientExample.class.getName());

    public static void main(String[] args) throws InterruptedException {
        String serverAddress = "localhost";
        int port = 1338;
        int amount = 100_000;
        final AtomicInteger total = new AtomicInteger();

        System.out.println("Starting " + amount + " sockets");

        for (int i = 0; i < amount; i++) {
            Thread.ofVirtual().start(() -> {
                try (Socket clientSocket = new Socket(serverAddress, port)) {
                    String dataToSend;
                    int rand = random.nextInt(0, 2);
                    if (rand == 0) {
                        dataToSend = "SET " + random.nextInt(1000) + 10_000 + " " + random.nextInt(100_000);
                    } else {
                        dataToSend = "GET " + random.nextInt(89_000) + 10_000 + " " + random.nextInt(100_000);
                    }

                    OutputStream outputStream = clientSocket.getOutputStream();
                    outputStream.write(dataToSend.getBytes());
                    outputStream.flush();

                    // Receive and print the response
                    byte[] responseBuffer = new byte[1024];
                    int bytesRead = clientSocket.getInputStream().read(responseBuffer);
                    String response = new String(responseBuffer, 0, bytesRead);
                    // System.out.println("Received from server: " + response);
                    total.getAndAdd(1);
                } catch (Exception ex) {
                    logger.log(Level.INFO, "FAILURE");
                    ex.printStackTrace();
                    logger.log(Level.INFO, ex.getMessage());
                }
            });
        }
        /* TODO wait until all threads are finished! */
        Thread.sleep(50000);
        logger.log(Level.INFO, "Total amount of threads created = " + total);
        System.out.println(total);
        logger.log(Level.INFO, "Finished one million connections!");
    }

}
