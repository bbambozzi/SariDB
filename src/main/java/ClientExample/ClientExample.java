package ClientExample;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;

public record ClientExample() {
    private static final Random random = new Random();
    private static final Logger logger = Logger.getLogger(ClientExample.class.getName());


    public static void main(String[] args) throws InterruptedException, ExecutionException {
        String serverAddress = args.length >= 1 ? args[0] : "localhost";
        int port = 1338;
        int amount = 50;
        AtomicInteger queries = new AtomicInteger();
        System.out.println("Starting " + amount + " sockets");
        List<Thread> threads = new ArrayList<>();
        Instant start = Instant.now();
        for (int i = 0; i < amount; i++) {
            threads.add(Thread.ofVirtual().start(() -> {
                try (Socket clientSocket = new Socket(serverAddress, port)) {
                    OutputStream outputStream = clientSocket.getOutputStream();
                    InputStream inputStream = clientSocket.getInputStream();
                    String dataToSend;
                    for (int j = 0; j < 100; j++) {
                        queries.getAndAdd(1);

                        int rand = random.nextInt(0, 2);
                        if (rand == 0) {
                            dataToSend = "SET " + random.nextInt(1000) + 10_000 + " " + random.nextInt(100_000);
                        } else {
                            dataToSend = "GET " + random.nextInt(1000) + 10_000 + " " + random.nextInt(100_000);
                        }

                        outputStream.write(dataToSend.getBytes());
                        outputStream.flush();

                        // Receive and print the response
                        byte[] responseBuffer = new byte[1024];
                        int bytesRead = inputStream.read(responseBuffer);
                        // System.out.println("Response = " + new String(responseBuffer));
                    }
                } catch (Exception ex) {
                    logger.log(Level.INFO, "FAILURE");
                    logger.log(Level.INFO, ex.getMessage());
                }
            }));
        }
        threads.forEach(
                th -> {
                    try {
                        th.join();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
        );
        Instant end = Instant.now();
        var timeTaken = Duration.between(start, end).toMillis();
        logger.log(Level.INFO, "Finished " + amount + " connections!");
        logger.log(Level.INFO, "Total queries=" + queries);
        logger.log(Level.INFO, "Time Taken=" + timeTaken + " milliseconds.");
        String formattedLog = String.format("TIME=%dms, QUERIES=%s, CONNECTIONS=%d ", timeTaken, queries, amount);
        logger.log(Level.INFO, formattedLog);
    }

}
