
import java.rmi.Naming;

public class RMIClient {

    public static void main(String[] args) {
        try {
            RemoteCalculator obj = (RemoteCalculator)
                    Naming.lookup("rmi://localhost:1099/CalculatorService");

            int a = 20;
            int b = 10;

            System.out.println("First Number: " + a);
            System.out.println("Second Number: " + b);

            System.out.println("Addition: " + obj.addition(a, b));
            System.out.println("Subtraction: " + obj.subtraction(a, b));
            System.out.println("Multiplication: " + obj.multiply(a, b));

        } catch (Exception e) {
            System.out.println("Client Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
