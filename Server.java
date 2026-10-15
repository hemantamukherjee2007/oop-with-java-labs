
import java.io.*;
import java.net.*;

public class Server {
    public static void main(String[] args) {
        try {
            ServerSocket ss = new ServerSocket(5000);
            System.out.println("Server is waiting for client...");

            Socket s = ss.accept();
            System.out.println("Client connected.");

            BufferedReader input = new BufferedReader(
                    new InputStreamReader(s.getInputStream()));

            PrintWriter output = new PrintWriter(
                    s.getOutputStream(), true);

            String message = input.readLine();
            System.out.println("Message from client: " + message);

            output.println("Hello Client, message received!");

            s.close();
            ss.close();

        } catch (IOException e) {
            System.out.println("Server error: " + e.getMessage());
        }
    }
}
