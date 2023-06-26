package PFA;

import javax.swing.*;
import java.awt.*;

public class ReportWindow extends JFrame {
    private JTextArea reportTextArea;
    
    // Constructs a new ReportWindow object
    // Initializes the window properties and components

    public ReportWindow() {
        setTitle("Report");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(400, 300);
        setLocationRelativeTo(null);

        reportTextArea = new JTextArea();
        reportTextArea.setEditable(false);

        JScrollPane scrollPane = new JScrollPane(reportTextArea);
        getContentPane().add(scrollPane, BorderLayout.CENTER);
    }
    
    // Displays the generated report in the report window.
    public void displayReport(String report) {
        reportTextArea.setText(report);
        setVisible(true);
    }
}
