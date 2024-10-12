import java.net.*;
import java.util.Scanner;

public class ChatClientUDP {
    private static final String SERVER_IP = "localhost"; // Ubah jika server berada di mesin lain
    private static final int SERVER_PORT = 9876;

    public static void main(String[] args) throws Exception {
        DatagramSocket clientSocket = new DatagramSocket();
        InetAddress serverAddress = InetAddress.getByName(SERVER_IP);
        Scanner scanner = new Scanner(System.in);

        Thread receiveThread = new Thread(() -> {
            byte[] receiveData = new byte[1024];
            while (true) {
                try {
                    DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                    clientSocket.receive(receivePacket);
                    String message = new String(receivePacket.getData(), 0, receivePacket.getLength());
                    System.out.println("Message from server: " + message);
                } catch (Exception e) {
                    e.printStackTrace();
                    break;
                }
            }
        });

        receiveThread.start();

        // Send messages to the server
        while (true) {
            System.out.print("You: ");
            String message = scanner.nextLine();
            byte[] sendData = message.getBytes();
            DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, serverAddress, SERVER_PORT);
            clientSocket.send(sendPacket);

            if (message.equalsIgnoreCase("bye")) {
                break;
            }
        }

        clientSocket.close();
        scanner.close();
    }
}
