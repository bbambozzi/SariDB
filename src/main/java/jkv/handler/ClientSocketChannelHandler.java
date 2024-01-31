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
                socketChannel.close();
                System.out.println("Closed? maybe?");
            } else if (bytesRead > 0) {
                buffer.flip();
                byte[] receivedBytes = new byte[buffer.remaining()];
                buffer.get(receivedBytes);
                String receivedData = new String(receivedBytes);
                buffer.clear();
                String response = "RECEIVED";
                ByteBuffer responseBuffer = ByteBuffer.wrap(response.getBytes());
                socketChannel.write(responseBuffer);
            }
        } catch (IOException e) {
            e.printStackTrace(); // todo log
        }
    }


    @Override
    public void run() {
        System.out.println("Running..");
        handleNewDataFromClient();
    }
}
