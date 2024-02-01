package client;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

public record ClientExample() {
    private static final Random random = new Random();
    private static final Logger logger = Logger.getLogger(ClientExample.class.getName());

    public static void main(String[] args) throws InterruptedException {
        String serverAddress = "localhost";
        int port = 1338;
        int amount = 100;
        var executor = Executors.newVirtualThreadPerTaskExecutor();

        System.out.println("Starting " + amount + " sockets");

        for (int i = 0; i < amount; i++) {
            executor.submit(() -> {
                try (Socket clientSocket = new Socket(serverAddress, port)) {
                    String dataToSend;
                    int rand = random.nextInt(0, 2);
                    OutputStream outputStream = clientSocket.getOutputStream();
                    InputStream inputStream = clientSocket.getInputStream();
                    for (int j = 0; j < 10; j++) {
                        if (rand == 0) {
                            dataToSend = "SET " + random.nextInt(1000) + 10_000 + " " + random.nextInt(100_000);
                        } else {
                            dataToSend = "GET " + random.nextInt(89_000) + 10_000 + " " + random.nextInt(100_000);
                        }

                        outputStream.write(dataToSend.getBytes());
                        outputStream.flush();
                        System.out.println("Sent " + dataToSend);

                        // Receive and print the response
                        byte[] responseBuffer = new byte[1024];
                        int bytesRead = inputStream.read(responseBuffer);
                        String response = new String(responseBuffer, 0, bytesRead);
                        System.out.println(response);
                    }
                    // System.out.println("Received from server: " + response);
                } catch (Exception ex) {
                    logger.log(Level.INFO, "FAILURE");
                    ex.printStackTrace();
                    logger.log(Level.INFO, ex.getMessage());
                }
            });
        }
        /* TODO wait until all threads are finished! */
        executor.awaitTermination(10000, TimeUnit.SECONDS);
        System.out.println("Finished!");
        logger.log(Level.INFO, "Finished one million connections!");
    }

}
