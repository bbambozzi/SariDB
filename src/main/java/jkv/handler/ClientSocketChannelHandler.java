package jkv.handler;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public record ClientSocketChannelHandler(SocketChannel socketChannel) implements Runnable {

    public void handleNewDataFromClient() {
        try {
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            int bytesRead = socketChannel.read(buffer);

            if (bytesRead == -1) {
                // Connection closed by the client
                socketChannel.close();
            } else if (bytesRead > 0) {
                buffer.flip();
                byte[] receivedBytes = new byte[buffer.remaining()];
                buffer.get(receivedBytes);
                String receivedData = new String(receivedBytes);
                System.out.println("Received: " + receivedData);
                buffer.clear();
            }
        } catch (IOException e) {
            e.printStackTrace(); // todo log
        }
    }


    @Override
    public void run() {
        // todo
        System.out.println("Running..");
    }
}
