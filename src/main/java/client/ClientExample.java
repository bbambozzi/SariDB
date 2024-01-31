package client;

import java.io.OutputStream;
import java.net.Socket;
import java.util.Random;

public record ClientExample() {
    public static void main(String[] args) {
        String serverAddress = "localhost";
        int port = 1338;
        int amount = 1_000_000;

        if (args.length >= 3) {
            amount = Integer.parseInt(args[2]);
        }

        System.out.println("Starting " + amount + " sockets");

        for (int i = 0; i < amount; i++) {
            Thread.ofVirtual().start(() -> {
                try (Socket clientSocket = new Socket(serverAddress, port)) {
                    // Create a socket object

                    // Send data to the server
                    Random random = new Random();
                    String dataToSend;
                    int rand = random.nextInt(2);
                    if (rand == 0) {
                        dataToSend = "SET " + random.nextInt(89_000) + 10_000 + " " + random.nextInt(100_000) + 1;
                    } else {
                        dataToSend = "GET " + random.nextInt(89_000) + 10_000 + " " + random.nextInt(100_000) + 1;
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
