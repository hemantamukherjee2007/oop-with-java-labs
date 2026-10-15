
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;

public class MatrixMultiplication {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(
                new InputStreamReader(System.in));

        int[][] a = new int[10][10];
        int[][] b = new int[10][10];
        int[][] c = new int[10][10];

        int r1, c1, r2, c2;

        System.out.println("Enter rows and columns of first matrix:");
        r1 = Integer.parseInt(br.readLine());
        c1 = Integer.parseInt(br.readLine());

        System.out.println("Enter rows and columns of second matrix:");
        r2 = Integer.parseInt(br.readLine());
        c2 = Integer.parseInt(br.readLine());

        if (c1 != r2) {
            System.out.println("Matrix multiplication not possible.");
            return;
        }

        if (r1 > 10 || c1 > 10 || r2 > 10 || c2 > 10
                || r1 <= 0 || c1 <= 0 || r2 <= 0 || c2 <= 0) {
            System.out.println("Invalid matrix dimensions.");
            return;
        }

        System.out.println("Enter elements of first matrix:");
        for (int i = 0; i < r1; i++) {
            for (int j = 0; j < c1; j++) {
                a[i][j] = Integer.parseInt(br.readLine());
            }
        }

        System.out.println("Enter elements of second matrix:");
        for (int i = 0; i < r2; i++) {
            for (int j = 0; j < c2; j++) {
                b[i][j] = Integer.parseInt(br.readLine());
            }
        }

        // Multiply the matrices
        for (int i = 0; i < r1; i++) {
            for (int j = 0; j < c2; j++) {
                for (int k = 0; k < c1; k++) {
                    c[i][j] += a[i][k] * b[k][j];
                }
            }
        }

        System.out.println("Product of the matrices:");
        for (int i = 0; i < r1; i++) {
            for (int j = 0; j < c2; j++) {
                System.out.print(c[i][j] + "\t");
            }
            System.out.println();
        }
    }
}
