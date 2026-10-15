
import java.io.*;
import java.net.*;

public class Client {
    public static void main(String[] args) {
        try {
            Socket s = new Socket("localhost", 5000);

            BufferedReader input = new BufferedReader(
                    new InputStreamReader(s.getInputStream()));

            PrintWriter output = new PrintWriter(
                    s.getOutputStream(), true);

            output.println("Hello Server!");

            String reply = input.readLine();
            System.out.println("Message from server: " + reply);

            s.close();

        } catch (IOException e) {
            System.out.println("Client error: " + e.getMessage());
        }
    }
}
