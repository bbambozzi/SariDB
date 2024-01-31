package client;

import java.io.OutputStream;
import java.net.Socket;
import java.util.Random;

public record ClientExample() {
    private static final Random random = new Random();
    public static void main(String[] args) {
        String serverAddress = "localhost";
        int port = 1338;
        int amount = 10_000_000;

        if (args.length >= 3) {
            amount = Integer.parseInt(args[2]);
        }

        System.out.println("Starting " + amount + " sockets");

        for (int i = 0; i < amount; i++) {
            Thread.ofVirtual().start(() -> {
                try (Socket clientSocket = new Socket(serverAddress, port)) {
                    String dataToSend;
                    int rand = random.nextInt(0, 2);
                    if (rand == 0) {
                        dataToSend = "SET " + random.nextInt(89_000) + 10_000 + " " + random.nextInt(100_000);
                    } else {
                        dataToSend = "GET " + random.nextInt(89_000) + 10_000 + " " + random.nextInt(100_000);
                    }

                    OutputStream outputStream = clientSocket.getOutputStream();
                    outputStream.write(dataToSend.getBytes());
                    clientSocket.shutdownOutput();

                    // Receive and print the response
                    byte[] responseBuffer = new byte[1024];
                    int bytesRead = clientSocket.getInputStream().read(responseBuffer);
                    String response = new String(responseBuffer, 0, bytesRead);
                    System.out.println("Received from server: " + response);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }
        System.out.println("Finished one million connections!");
    }

}
