
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

public class Notepad extends JFrame implements ActionListener {

    JTextArea textArea;
    JFileChooser fileChooser;
    File currentFile = null;

    JMenuItem newFile, openFile, saveFile, saveAs, exit;
    JMenuItem cut, copy, paste, selectAll;
    JCheckBoxMenuItem wordWrap;

    public Notepad() {
        setTitle("Untitled - Notepad");
        setSize(700, 500);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

        textArea = new JTextArea();
        textArea.setFont(new Font("Arial", Font.PLAIN, 16));
        textArea.setTabSize(4);

        JScrollPane scrollPane = new JScrollPane(textArea);
        add(scrollPane, BorderLayout.CENTER);

        fileChooser = new JFileChooser();

        JMenuBar menuBar = new JMenuBar();

        // File Menu
        JMenu fileMenu = new JMenu("File");

        newFile = new JMenuItem("New");
        openFile = new JMenuItem("Open");
        saveFile = new JMenuItem("Save");
        saveAs = new JMenuItem("Save As");
        exit = new JMenuItem("Exit");

        fileMenu.add(newFile);
        fileMenu.add(openFile);
        fileMenu.add(saveFile);
        fileMenu.add(saveAs);
        fileMenu.addSeparator();
        fileMenu.add(exit);

        // Edit Menu
        JMenu editMenu = new JMenu("Edit");

        cut = new JMenuItem("Cut");
        copy = new JMenuItem("Copy");
        paste = new JMenuItem("Paste");
        selectAll = new JMenuItem("Select All");

        editMenu.add(cut);
        editMenu.add(copy);
        editMenu.add(paste);
        editMenu.addSeparator();
        editMenu.add(selectAll);

        // Format Menu
        JMenu formatMenu = new JMenu("Format");

        wordWrap = new JCheckBoxMenuItem("Word Wrap");
        wordWrap.setSelected(true);
        formatMenu.add(wordWrap);

        menuBar.add(fileMenu);
        menuBar.add(editMenu);
        menuBar.add(formatMenu);

        setJMenuBar(menuBar);

        // Register listeners
        newFile.addActionListener(this);
        openFile.addActionListener(this);
        saveFile.addActionListener(this);
        saveAs.addActionListener(this);
        exit.addActionListener(this);

        cut.addActionListener(this);
        copy.addActionListener(this);
        paste.addActionListener(this);
        selectAll.addActionListener(this);
        wordWrap.addActionListener(this);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                exitApplication();
            }
        });

        setLocationRelativeTo(null);
        setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();

        if (source == newFile) {
            if (confirmDiscard()) {
                textArea.setText("");
                currentFile = null;
                setTitle("Untitled - Notepad");
            }
        } else if (source == openFile) {
            open();
        } else if (source == saveFile) {
            save();
        } else if (source == saveAs) {
            saveAs();
        } else if (source == exit) {
            exitApplication();
        } else if (source == cut) {
            textArea.cut();
        } else if (source == copy) {
            textArea.copy();
        } else if (source == paste) {
            textArea.paste();
        } else if (source == selectAll) {
            textArea.selectAll();
        } else if (source == wordWrap) {
            boolean wrap = wordWrap.isSelected();
            textArea.setLineWrap(wrap);
            textArea.setWrapStyleWord(wrap);
        }
    }

    private boolean confirmDiscard() {
        if (textArea.getText().isEmpty()) {
            return true;
        }

        int choice = JOptionPane.showConfirmDialog(
                this,
                "Discard current text?",
                "Confirm",
                JOptionPane.YES_NO_OPTION
        );

        return choice == JOptionPane.YES_OPTION;
    }

    private void open() {
        if (!confirmDiscard()) {
            return;
        }

        if (fileChooser.showOpenDialog(this)
                == JFileChooser.APPROVE_OPTION) {
            currentFile = fileChooser.getSelectedFile();

            try (BufferedReader reader = new BufferedReader(
                    new FileReader(currentFile))) {
                textArea.read(reader, null);
                setTitle(currentFile.getName() + " - Notepad");
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(
                        this, "Error opening file: " + ex.getMessage());
            }
        }
    }

    private void save() {
        if (currentFile == null) {
            saveAs();
            return;
        }

        writeFile(currentFile);
    }

    private void saveAs() {
        if (fileChooser.showSaveDialog(this)
                == JFileChooser.APPROVE_OPTION) {
            currentFile = fileChooser.getSelectedFile();
            writeFile(currentFile);
        }
    }

    private void writeFile(File file) {
        try (BufferedWriter writer = new BufferedWriter(
                new FileWriter(file))) {
            writer.write(textArea.getText());
            setTitle(file.getName() + " - Notepad");
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(
                    this, "Error saving file: " + ex.getMessage());
        }
    }

    private void exitApplication() {
        if (confirmDiscard()) {
            dispose();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Notepad::new);
    }
}
