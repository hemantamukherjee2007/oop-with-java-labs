
import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.nio.charset.StandardCharsets;

public class StudentRegistrationPortal extends JFrame {

    private JTextField nameField;
    private JTextField rollField;
    private JTextField emailField;
    private JTextField courseField;
    private JTextField phoneField;
    private JComboBox<String> genderBox;
    private JLabel statusLabel;

    private static final String FILE_NAME = "students.csv";

    public StudentRegistrationPortal() {
        setTitle("Student Registration Portal");
        setSize(500, 430);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(8, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 25, 20, 25));

        panel.add(new JLabel("Student Name:"));
        nameField = new JTextField();
        panel.add(nameField);

        panel.add(new JLabel("Roll Number:"));
        rollField = new JTextField();
        panel.add(rollField);

        panel.add(new JLabel("Email Address:"));
        emailField = new JTextField();
        panel.add(emailField);

        panel.add(new JLabel("Phone Number:"));
        phoneField = new JTextField();
        panel.add(phoneField);

        panel.add(new JLabel("Course:"));
        courseField = new JTextField();
        panel.add(courseField);

        panel.add(new JLabel("Gender:"));
        genderBox = new JComboBox<>(
                new String[]{"Select", "Male", "Female", "Other"}
        );
        panel.add(genderBox);

        JButton registerButton = new JButton("Register");
        JButton clearButton = new JButton("Clear Form");

        panel.add(registerButton);
        panel.add(clearButton);

        statusLabel = new JLabel("Enter student details.");
        panel.add(statusLabel);

        JButton viewButton = new JButton("View Registrations");
        panel.add(viewButton);

        registerButton.addActionListener(e -> registerStudent());
        clearButton.addActionListener(e -> clearForm());
        viewButton.addActionListener(e -> viewRegistrations());

        add(panel);
        setVisible(true);
    }

    private void registerStudent() {
        String name = nameField.getText().trim();
        String roll = rollField.getText().trim();
        String email = emailField.getText().trim();
        String phone = phoneField.getText().trim();
        String course = courseField.getText().trim();
        String gender = (String) genderBox.getSelectedItem();

        if (name.isEmpty() || roll.isEmpty() || email.isEmpty()
                || phone.isEmpty() || course.isEmpty()
                || "Select".equals(gender)) {
            statusLabel.setText("Please fill in every field.");
            return;
        }

        File file = new File(FILE_NAME);
        boolean writeHeader = !file.exists() || file.length() == 0;

        try (PrintWriter writer = new PrintWriter(
                new OutputStreamWriter(
                        new FileOutputStream(file, true),
                        StandardCharsets.UTF_8))) {

            if (writeHeader) {
                writer.println(
                        "Name,Roll Number,Email,Phone,Course,Gender"
                );
            }

            writer.println(
                    csv(name) + "," +
                            csv(roll) + "," +
                            csv(email) + "," +
                            csv(phone) + "," +
                            csv(course) + "," +
                            csv(gender)
            );

            statusLabel.setText("Registration successful!");
            JOptionPane.showMessageDialog(
                    this,
                    "Student registered successfully!\n" +
                            "Saved to: " + file.getAbsolutePath()
            );

            clearForm();

        } catch (IOException ex) {
            statusLabel.setText("Error saving registration.");
            JOptionPane.showMessageDialog(
                    this,
                    "Could not save student data:\n" + ex.getMessage(),
                    "File Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    // Escapes CSV values correctly, including commas and quotation marks.
    private String csv(String value) {
        return "\"" + value.replace("\"", "\"\"") + "\"";
    }

    private void clearForm() {
        nameField.setText("");
        rollField.setText("");
        emailField.setText("");
        phoneField.setText("");
        courseField.setText("");
        genderBox.setSelectedIndex(0);
        statusLabel.setText("Enter student details.");
    }

    private void viewRegistrations() {
        File file = new File(FILE_NAME);

        if (!file.exists() || file.length() == 0) {
            JOptionPane.showMessageDialog(
                    this, "No student registrations found."
            );
            return;
        }

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(
                        new FileInputStream(file),
                        StandardCharsets.UTF_8))) {

            JTextArea textArea = new JTextArea();
            textArea.setEditable(false);
            textArea.setFont(new Font("Monospaced", Font.PLAIN, 12));

            String line;
            while ((line = reader.readLine()) != null) {
                textArea.append(line + "\n");
            }

            JOptionPane.showMessageDialog(
                    this,
                    new JScrollPane(textArea),
                    "Student Registrations",
                    JOptionPane.INFORMATION_MESSAGE
            );

        } catch (IOException ex) {
            JOptionPane.showMessageDialog(
                    this,
                    "Could not read file: " + ex.getMessage(),
                    "File Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(StudentRegistrationPortal::new);
    }
}
