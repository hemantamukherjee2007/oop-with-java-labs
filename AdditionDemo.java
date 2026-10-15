
import java.util.Scanner;

public class AdditionDemo {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        AdditionBean bean = new AdditionBean();

        System.out.print("Enter first number: ");
        bean.setNumber1(sc.nextInt());

        System.out.print("Enter second number: ");
        bean.setNumber2(sc.nextInt());

        bean.add();

        System.out.println("Sum = " + bean.getResult());

        sc.close();
    }
}
