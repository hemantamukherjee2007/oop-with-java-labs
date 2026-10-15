
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.nio.charset.StandardCharsets;

public class LoginForm extends JFrame implements ActionListener {

    JLabel nameLabel, passwordLabel, resultLabel;
    JTextField nameField;
    JPasswordField passwordField;
    JButton submitButton;

    private static final String FILE_NAME = "users.csv";

    public LoginForm() {
        setTitle("Registration Form");
        setSize(400, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(3, 2, 10, 10));

        nameLabel = new JLabel("Name:");
        nameField = new JTextField();

        passwordLabel = new JLabel("Password:");
        passwordField = new JPasswordField();

        submitButton = new JButton("Submit");
        resultLabel = new JLabel("");

        add(nameLabel);
        add(nameField);
        add(passwordLabel);
        add(passwordField);
        add(submitButton);
        add(resultLabel);

        submitButton.addActionListener(this);

        setLocationRelativeTo(null);
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        String name = nameField.getText().trim();
        char[] passwordChars = passwordField.getPassword();

        if (name.isEmpty() || passwordChars.length == 0) {
            resultLabel.setText("Fill all fields!");
            java.util.Arrays.fill(passwordChars, '\0');
            return;
        }

        String password = new String(passwordChars);
        java.util.Arrays.fill(passwordChars, '\0');

        File file = new File(FILE_NAME);
        boolean writeHeader = !file.exists() || file.length() == 0;

        try (PrintWriter writer = new PrintWriter(
                new OutputStreamWriter(
                        new FileOutputStream(file, true),
                        StandardCharsets.UTF_8))) {

            if (writeHeader) {
                writer.println("Name,Password");
            }

            writer.println(
                    "\"" + name.replace("\"", "\"\"") + "\","
                            + "\"" + password.replace("\"", "\"\"") + "\""
            );

            resultLabel.setText("Data saved successfully!");
            nameField.setText("");
            passwordField.setText("");

        } catch (IOException ex) {
            resultLabel.setText("Error saving data!");
            JOptionPane.showMessageDialog(
                    this,
                    ex.getMessage(),
                    "File Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(LoginForm::new);
    }
}
