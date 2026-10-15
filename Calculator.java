
import java.awt.*;
import java.awt.event.*;

public class Calculator extends Frame
        implements ActionListener {

    private TextField display;
    private double num1 = 0;
    private String operator = "";
    private boolean newNumber = true;
    private boolean degrees = true;

    public Calculator() {
        setTitle("Scientific Calculator");
        setSize(420, 520);
        setLayout(new BorderLayout(5, 5));

        display = new TextField("0");
        display.setEditable(false);
        display.setFont(new Font("Arial", Font.BOLD, 25));
        display.setText("0");
        add(display, BorderLayout.NORTH);

        Panel panel = new Panel();
        panel.setLayout(new GridLayout(6, 5, 5, 5));

        String[] buttons = {
                "sin", "cos", "tan", "DEG/RAD", "C",
                "asin", "acos", "atan", "sqrt", "x^2",
                "log", "ln", "1/x", "x^y", "sqrt3",
                "7", "8", "9", "/", "%",
                "4", "5", "6", "*", "!",
                "1", "2", "3", "-", "pi",
                "0", ".", "=", "+", "e"
        };

        panel.setLayout(new GridLayout(7, 5, 5, 5));

        for (String text : buttons) {
            Button b = new Button(text);
            b.setFont(new Font("Arial", Font.BOLD, 15));
            b.addActionListener(this);
            panel.add(b);
        }

        add(panel, BorderLayout.CENTER);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                dispose();
            }
        });

        setLocationRelativeTo(null);
        setVisible(true);
    }

    private double toRadians(double value) {
        return degrees ? Math.toRadians(value) : value;
    }

    private double fromRadians(double value) {
        return degrees ? Math.toDegrees(value) : value;
    }

    private double factorial(double n) {
        if (n < 0 || n != Math.floor(n) || n > 170) {
            throw new ArithmeticException("Invalid factorial");
        }

        double result = 1;
        for (int i = 2; i <= (int) n; i++) {
            result *= i;
        }
        return result;
    }

    private void calculate() {
        double num2 = Double.parseDouble(display.getText());
        double result;

        switch (operator) {
            case "+": result = num1 + num2; break;
            case "-": result = num1 - num2; break;
            case "*": result = num1 * num2; break;
            case "/":
                if (num2 == 0) {
                    throw new ArithmeticException("Division by zero");
                }
                result = num1 / num2;
                break;
            case "%": result = num1 % num2; break;
            case "x^y": result = Math.pow(num1, num2); break;
            default: return;
        }

        display.setText(format(result));
        operator = "";
        newNumber = true;
    }

    private String format(double value) {
        if (Double.isNaN(value) || Double.isInfinite(value)) {
            return "Math Error";
        }
        if (value == Math.rint(value)
                && Math.abs(value) < 1.0e15) {
            return String.valueOf((long) value);
        }
        return String.valueOf(value);
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        String cmd = event.getActionCommand();

        try {
            if (cmd.matches("[0-9]")) {
                if (newNumber || display.getText().equals("0")
                        || display.getText().equals("Math Error")) {
                    display.setText(cmd);
                    newNumber = false;
                } else {
                    display.setText(display.getText() + cmd);
                }
                return;
            }

            if (cmd.equals(".")) {
                if (newNumber) {
                    display.setText("0.");
                    newNumber = false;
                } else if (!display.getText().contains(".")) {
                    display.setText(display.getText() + ".");
                }
                return;
            }

            if (cmd.equals("C")) {
                display.setText("0");
                num1 = 0;
                operator = "";
                newNumber = true;
                return;
            }

            if (cmd.equals("DEG/RAD")) {
                degrees = !degrees;
                display.setText(degrees ? "DEG mode" : "RAD mode");
                newNumber = true;
                return;
            }

            if (cmd.equals("pi")) {
                display.setText(format(Math.PI));
                newNumber = true;
                return;
            }

            if (cmd.equals("e")) {
                display.setText(format(Math.E));
                newNumber = true;
                return;
            }

            double value = Double.parseDouble(display.getText());

            switch (cmd) {
                case "sin":
                    display.setText(format(Math.sin(toRadians(value))));
                    newNumber = true;
                    return;
                case "cos":
                    display.setText(format(Math.cos(toRadians(value))));
                    newNumber = true;
                    return;
                case "tan":
                    if (degrees && Math.abs(value % 180) == 90) {
                        throw new ArithmeticException("Undefined tangent");
                    }
                    display.setText(format(Math.tan(toRadians(value))));
                    newNumber = true;
                    return;
                case "asin":
                    display.setText(format(fromRadians(Math.asin(value))));
                    newNumber = true;
                    return;
                case "acos":
                    display.setText(format(fromRadians(Math.acos(value))));
                    newNumber = true;
                    return;
                case "atan":
                    display.setText(format(fromRadians(Math.atan(value))));
                    newNumber = true;
                    return;
                case "sqrt":
                    if (value < 0) {
                        throw new ArithmeticException("Invalid square root");
                    }
                    display.setText(format(Math.sqrt(value)));
                    newNumber = true;
                    return;
                case "sqrt3":
                    display.setText(format(Math.cbrt(value)));
                    newNumber = true;
                    return;
                case "x^2":
                    display.setText(format(value * value));
                    newNumber = true;
                    return;
                case "1/x":
                    if (value == 0) {
                        throw new ArithmeticException("Division by zero");
                    }
                    display.setText(format(1 / value));
                    newNumber = true;
                    return;
                case "log":
                    if (value <= 0) {
                        throw new ArithmeticException("Invalid logarithm");
                    }
                    display.setText(format(Math.log10(value)));
                    newNumber = true;
                    return;
                case "ln":
                    if (value <= 0) {
                        throw new ArithmeticException("Invalid logarithm");
                    }
                    display.setText(format(Math.log(value)));
                    newNumber = true;
                    return;
                case "!":
                    display.setText(format(factorial(value)));
                    newNumber = true;
                    return;
                case "=":
                    calculate();
                    return;
                case "+":
                case "-":
                case "*":
                case "/":
                case "%":
                case "x^y":
                    num1 = value;
                    operator = cmd;
                    newNumber = true;
                    return;
            }

        } catch (ArithmeticException | NumberFormatException ex) {
            display.setText("Math Error");
            operator = "";
            newNumber = true;
        }
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(Calculator::new);
    }
}
