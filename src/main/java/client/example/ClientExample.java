package client.example;

import client.SariDBClient;

import java.net.Socket;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;

public record ClientExample() {
    private static final Logger logger = Logger.getLogger(ClientExample.class.getName());


    public static void main(String[] args) throws InterruptedException, ExecutionException {
        String serverAddress = args.length >= 1 ? args[0] : "localhost";
        int port = 1338;
        int amount = 1;
        AtomicInteger queries = new AtomicInteger();
        System.out.println("Starting " + amount + " sockets");
        List<Thread> threads = new ArrayList<>();
        Instant start = Instant.now();
        for (int i = 0; i < amount; i++) {
            threads.add(Thread.ofPlatform().start(() -> {
                try (Socket clientSocket = new Socket(serverAddress, port)) {
                    SariDBClient sariDBClient = new SariDBClient(clientSocket.getInputStream(), clientSocket.getOutputStream());
                } catch (Exception ex) {
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
        logger.log(Level.INFO, String.format("TIME=%dms, QUERIES=%s, CONNECTIONS=%d ", timeTaken, queries, amount));
    }

}
