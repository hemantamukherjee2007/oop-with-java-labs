
import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class StudentLogin extends JFrame {

    private JTextField nameField;
    private JPasswordField passwordField;
    private JLabel statusLabel;

    private static final String FILE_NAME = "students.csv";

    public StudentLogin() {
        setTitle("Student CRUD Operations");
        setSize(500, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        setLayout(new BorderLayout(10, 10));

        JPanel formPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        formPanel.setBorder(
                BorderFactory.createEmptyBorder(20, 20, 10, 20)
        );

        formPanel.add(new JLabel("Name:"));
        nameField = new JTextField();
        formPanel.add(nameField);

        formPanel.add(new JLabel("Password:"));
        passwordField = new JPasswordField();
        formPanel.add(passwordField);

        JButton createButton = new JButton("Create");
        JButton readButton = new JButton("Read");
        JButton updateButton = new JButton("Update");
        JButton deleteButton = new JButton("Delete");

        JPanel buttonPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        buttonPanel.add(createButton);
        buttonPanel.add(readButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);

        statusLabel = new JLabel("Enter name and password.");
        statusLabel.setBorder(
                BorderFactory.createEmptyBorder(0, 20, 15, 20)
        );

        add(formPanel, BorderLayout.NORTH);
        add(buttonPanel, BorderLayout.CENTER);
        add(statusLabel, BorderLayout.SOUTH);

        createButton.addActionListener(e -> createRecord());
        readButton.addActionListener(e -> readRecords());
        updateButton.addActionListener(e -> updateRecord());
        deleteButton.addActionListener(e -> deleteRecord());

        setVisible(true);
    }

    // CREATE: Add a new record
    private void createRecord() {
        String name = nameField.getText().trim();
        char[] passwordChars = passwordField.getPassword();

        if (name.isEmpty() || passwordChars.length == 0) {
            statusLabel.setText("Enter both name and password.");
            java.util.Arrays.fill(passwordChars, '\0');
            return;
        }

        String password = new String(passwordChars);
        java.util.Arrays.fill(passwordChars, '\0');

        try {
            List<String[]> records = loadRecords();

            for (String[] record : records) {
                if (record[0].equalsIgnoreCase(name)) {
                    statusLabel.setText("Name already exists.");
                    return;
                }
            }

            records.add(new String[]{name, password});
            saveRecords(records);

            statusLabel.setText("Record created successfully!");
            clearFields();

        } catch (IOException ex) {
            showError(ex);
        }
    }

    // READ: Display all records
    private void readRecords() {
        try {
            List<String[]> records = loadRecords();

            if (records.isEmpty()) {
                JOptionPane.showMessageDialog(
                        this, "No records found."
                );
                return;
            }

            StringBuilder output = new StringBuilder();
            output.append("Name\tPassword\n");
            output.append("-------------------------------\n");

            for (String[] record : records) {
                output.append(record[0])
                        .append("\t")
                        .append(record[1])
                        .append("\n");
            }

            JTextArea area = new JTextArea(output.toString(), 15, 35);
            area.setEditable(false);

            JOptionPane.showMessageDialog(
                    this,
                    new JScrollPane(area),
                    "Student Records",
                    JOptionPane.INFORMATION_MESSAGE
            );

        } catch (IOException ex) {
            showError(ex);
        }
    }

    // UPDATE: Change password for an existing name
    private void updateRecord() {
        String name = nameField.getText().trim();
        char[] passwordChars = passwordField.getPassword();

        if (name.isEmpty() || passwordChars.length == 0) {
            statusLabel.setText("Enter name and new password.");
            java.util.Arrays.fill(passwordChars, '\0');
            return;
        }

        String newPassword = new String(passwordChars);
        java.util.Arrays.fill(passwordChars, '\0');

        try {
            List<String[]> records = loadRecords();
            boolean found = false;

            for (String[] record : records) {
                if (record[0].equalsIgnoreCase(name)) {
                    record[1] = newPassword;
                    found = true;
                    break;
                }
            }

            if (found) {
                saveRecords(records);
                statusLabel.setText("Password updated successfully!");
                clearFields();
            } else {
                statusLabel.setText("Name not found.");
            }

        } catch (IOException ex) {
            showError(ex);
        }
    }

    // DELETE: Remove a record by name
    private void deleteRecord() {
        String name = nameField.getText().trim();

        if (name.isEmpty()) {
            statusLabel.setText("Enter the name to delete.");
            return;
        }

        int choice = JOptionPane.showConfirmDialog(
                this,
                "Delete the record for " + name + "?",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION
        );

        if (choice != JOptionPane.YES_OPTION) {
            return;
        }

        try {
            List<String[]> records = loadRecords();
            boolean removed = records.removeIf(
                    record -> record[0].equalsIgnoreCase(name)
            );

            if (removed) {
                saveRecords(records);
                statusLabel.setText("Record deleted successfully!");
                clearFields();
            } else {
                statusLabel.setText("Name not found.");
            }

        } catch (IOException ex) {
            showError(ex);
        }
    }

    // Read CSV records
    private List<String[]> loadRecords() throws IOException {
        List<String[]> records = new ArrayList<>();
        File file = new File(FILE_NAME);

        if (!file.exists()) {
            return records;
        }

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(
                        new FileInputStream(file),
                        StandardCharsets.UTF_8))) {

            String line;
            boolean firstLine = true;

            while ((line = reader.readLine()) != null) {
                if (firstLine) {
                    firstLine = false;
                    if (line.equals("\"Name\",\"Password\"")) {
                        continue;
                    }
                }

                List<String> fields = parseCsvLine(line);

                if (fields.size() >= 2) {
                    records.add(new String[]{
                            fields.get(0), fields.get(1)
                    });
                }
            }
        }

        return records;
    }

    // Save all records back to CSV
    private void saveRecords(List<String[]> records)
            throws IOException {

        try (PrintWriter writer = new PrintWriter(
                new OutputStreamWriter(
                        new FileOutputStream(FILE_NAME),
                        StandardCharsets.UTF_8))) {

            writer.println("\"Name\",\"Password\"");

            for (String[] record : records) {
                writer.println(csv(record[0]) + "," + csv(record[1]));
            }
        }
    }

    // Quote and escape CSV values
    private String csv(String value) {
        return "\"" + value.replace("\"", "\"\"") + "\"";
    }

    // Parse quoted CSV fields, including commas and escaped quotes
    private List<String> parseCsvLine(String line) {
        List<String> fields = new ArrayList<>();
        StringBuilder field = new StringBuilder();
        boolean quoted = false;

        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);

            if (c == '"') {
                if (quoted && i + 1 < line.length()
                        && line.charAt(i + 1) == '"') {
                    field.append('"');
                    i++;
                } else {
                    quoted = !quoted;
                }
            } else if (c == ',' && !quoted) {
                fields.add(field.toString());
                field.setLength(0);
            } else {
                field.append(c);
            }
        }

        fields.add(field.toString());
        return fields;
    }

    private void clearFields() {
        nameField.setText("");
        passwordField.setText("");
    }

    private void showError(IOException ex) {
        statusLabel.setText("File operation failed!");
        JOptionPane.showMessageDialog(
                this,
                ex.getMessage(),
                "File Error",
                JOptionPane.ERROR_MESSAGE
        );
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(StudentLogin::new);
    }
}
