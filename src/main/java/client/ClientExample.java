package client;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.logging.Logger;

public record ClientExample() {
    private static final Random random = new Random();
    private static final Logger logger = Logger.getLogger(ClientExample.class.getName());

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        String serverAddress = "localhost";
        int port = 1338;
        int amount = 10_000;
        var executor = Executors.newVirtualThreadPerTaskExecutor();

        System.out.println("Starting " + amount + " sockets");

        List<Future<?>> futures = new ArrayList<>();
        for (int i = 0; i < amount; i++) {
            futures.add(executor.submit(() -> {
                try (Socket clientSocket = new Socket(serverAddress, port)) {
                    String dataToSend;
                    int rand = random.nextInt(0, 2);
                    OutputStream outputStream = clientSocket.getOutputStream();
                    InputStream inputStream = clientSocket.getInputStream();
                    if (rand == 0) {
                        dataToSend = "SET " + random.nextInt(1000) + 10_000 + " " + random.nextInt(100_000);
                    } else {
                        dataToSend = "GET " + random.nextInt(89_000) + 10_000 + " " + random.nextInt(100_000);
                    }

                    outputStream.write(dataToSend.getBytes());
                    outputStream.flush();

                    // Receive and print the response
                    byte[] responseBuffer = new byte[1024];
                    int bytesRead = inputStream.read(responseBuffer);
                } catch (Exception ex) {
                    logger.log(Level.INFO, "FAILURE");
                    logger.log(Level.INFO, ex.getMessage());
                }
            }));
        }
        /* TODO wait until all threads are finished! */
        futures
                .stream()
                .parallel()
                .forEach(elem -> {
                    try {
                        elem.get();
                    } catch (Exception ignored) {
                        System.out.println("Failure getting thread value");
                    }
                });
        logger.log(Level.INFO, "Finished " + amount + " connections!");
        executor.close();
    }

}
