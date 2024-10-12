import java.io.*;
import java.net.*;
import java.util.Scanner;

public class FileClientTCP {
    private static final String SERVER_IP = "localhost"; // Adjust if server is on a different machine
    private static final int SERVER_PORT = 8080;

    public static void main(String[] args) {
        try (Socket socket = new Socket(SERVER_IP, SERVER_PORT);
             DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
             DataInputStream dis = new DataInputStream(socket.getInputStream());
             Scanner scanner = new Scanner(System.in)) {

            // Ask for file path from the user
            System.out.print("Enter file path to send: ");
            String filePath = scanner.nextLine();

            File file = new File(filePath);
            if (!file.exists()) {
                System.out.println("File does not exist.");
                return;
            }

            // Send file name and size to the server
            dos.writeUTF(file.getName());
            dos.writeLong(file.length());

            // Send the file data to the server
            try (FileInputStream fis = new FileInputStream(file)) {
                byte[] buffer = new byte[4096];
                int bytesRead;
                while ((bytesRead = fis.read(buffer)) > 0) {
                    dos.write(buffer, 0, bytesRead);
                }
            }

            // Receive confirmation from the server
            String response = dis.readUTF();
            System.out.println("Server response: " + response);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
