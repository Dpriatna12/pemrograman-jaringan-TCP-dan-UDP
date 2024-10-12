import java.net.*;
import java.util.*;

public class ChatServerUDP {
    private static final int PORT = 9876;
    private static final List<InetSocketAddress> clientAddresses = Collections.synchronizedList(new ArrayList<>());

    public static void main(String[] args) throws Exception {
        try (DatagramSocket serverSocket = new DatagramSocket(PORT)) {
            byte[] receiveData = new byte[1024];
            System.out.println("Server is running on port " + PORT);

            while (true) {
                DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                serverSocket.receive(receivePacket);

                // Get client information
                InetAddress clientAddress = receivePacket.getAddress();
                int clientPort = receivePacket.getPort();
                InetSocketAddress clientSocketAddress = new InetSocketAddress(clientAddress, clientPort);

                // Add client to the list if not already present
                if (!clientAddresses.contains(clientSocketAddress)) {
                    clientAddresses.add(clientSocketAddress);
                    System.out.println("New client connected: " + clientSocketAddress);
                }

                String message = new String(receivePacket.getData(), 0, receivePacket.getLength());
                System.out.println("Received message: " + message + " from " + clientAddress);

                // Broadcast message to all connected clients
                for (InetSocketAddress client : clientAddresses) {
                    DatagramPacket sendPacket = new DatagramPacket(message.getBytes(), message.length(), client.getAddress(), client.getPort());
                    serverSocket.send(sendPacket);
                }
            }
        }
    }
}
