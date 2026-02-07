import javax.swing.*;
import java.awt.*;

public class WGUI extends JFrame {

    private JTextField txtStudents, txtMidterm, txtFinals;
    private JComboBox<String> classBox;
    private JTextArea outputArea;

    private int totalStudents = 0;
    private int enteredStudents = 0;
    private int passed = 0;
    private int failed = 0;

    private double requiredPassingRate = 0.70;

    public WGUI() {

        setTitle("Student Grade Checker");
        setSize(550, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // ===== TOP PANEL =====
        JPanel topPanel = new JPanel(new GridLayout(4, 2, 5, 5));

        topPanel.add(new JLabel("Select Class:"));
        classBox = new JComboBox<>(new String[]{
                "ITC I21 - Computer Programming 2",
                "ITC 122 - Info Management"
        });
        topPanel.add(classBox);

        topPanel.add(new JLabel("Number of Students (0–99):"));
        txtStudents = new JTextField();
        topPanel.add(txtStudents);

        topPanel.add(new JLabel("Midterm Grade:"));
        txtMidterm = new JTextField();
        topPanel.add(txtMidterm);

        topPanel.add(new JLabel("Finals Grade:"));
        txtFinals = new JTextField();
        topPanel.add(txtFinals);

        add(topPanel, BorderLayout.NORTH);

        // ===== BUTTONS =====
        JPanel buttonPanel = new JPanel();

        JButton btnSet = new JButton("Set Students");
        JButton btnAdd = new JButton("Add Student Grade");
        JButton btnResult = new JButton("Show Result");

        buttonPanel.add(btnSet);
        buttonPanel.add(btnAdd);
        buttonPanel.add(btnResult);

        add(buttonPanel, BorderLayout.CENTER);

        // ===== OUTPUT =====
        outputArea = new JTextArea();
        outputArea.setEditable(false);
        outputArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        add(new JScrollPane(outputArea), BorderLayout.SOUTH);

        // ===== ACTIONS =====

        btnSet.addActionListener(e -> {
            try {
                totalStudents = Integer.parseInt(txtStudents.getText());

                if (totalStudents < 0 || totalStudents > 99) {
                    JOptionPane.showMessageDialog(this, "Students must be between 0 and 99");
                    return;
                }

                enteredStudents = 0;
                passed = 0;
                failed = 0;

                requiredPassingRate =
                        classBox.getSelectedIndex() == 0 ? 0.70 : 0.40;

                outputArea.setText("Students set to " + totalStudents + "\n");
                outputArea.append("---------------------------------------------\n");
                outputArea.append(String.format("%-10s %-10s %-10s %-10s %-10s\n",
                        "Student", "Midterm", "Finals", "Final", "Status"));
                outputArea.append("---------------------------------------------\n");

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Enter a valid integer");
            }
        });

        btnAdd.addActionListener(e -> {
            if (enteredStudents >= totalStudents) {
                JOptionPane.showMessageDialog(this, "All students already entered");
                return;
            }

            try {
                double mid = Double.parseDouble(txtMidterm.getText());
                double fin = Double.parseDouble(txtFinals.getText());

                double finalGrade = (mid * 0.40) + (fin * 0.60);
                String status = finalGrade >= 75 ? "PASSED" : "FAILED";

                if (finalGrade >= 75) {
                    passed++;
                } else {
                    failed++;
                }

                enteredStudents++;
                txtMidterm.setText("");
                txtFinals.setText("");

                outputArea.append(String.format("%-10d %-10.2f %-10.2f %-10.2f %-10s\n",
                        enteredStudents, mid, fin, finalGrade, status));

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Grades must be numeric (int or double)");
            }
        });

        btnResult.addActionListener(e -> {

            if (totalStudents == 0) return;

            double passPercent = ((double) passed / totalStudents) * 100;
            double failPercent = ((double) failed / totalStudents) * 100;

            outputArea.append("\n===== RESULT SUMMARY =====\n");
            outputArea.append("Failed students: " + failed +
                    ", Passed students: " + passed + "\n");
            outputArea.append("There are " + passed +
                    " students passed out of " + totalStudents + "\n");
            outputArea.append("The % value of students passed is: " +
                    String.format("%.2f", passPercent) + "%\n");
            outputArea.append("The value of students failed is: " +
                    String.format("%.2f", failPercent) + "%\n");
            outputArea.append("The Ratio is " + passed + " : " + failed + "\n");

            if (passPercent >= requiredPassingRate * 100) {
                outputArea.append("Class PASSED the required passing rate\n");
            } else {
                outputArea.append("Class FAILED the required passing rate\n");
            }
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new WGUI().setVisible(true));
    }
}
