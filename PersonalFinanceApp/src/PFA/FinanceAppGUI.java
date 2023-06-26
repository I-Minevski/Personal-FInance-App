package PFA;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;

public class FinanceAppGUI extends JFrame {
	// GUI components
    private JRadioButton positiveRadioButton;
    private JRadioButton negativeRadioButton;
    private JTextField amountTextField;
    private JTextField descriptionTextField;
    private JTextField dayTextField;
    private JTextField monthTextField;
    private JTextField yearTextField;
    private JButton addTransactionButton;
    private JButton exportButton;
    private JButton importButton;
    private JButton generateReportButton;
    private JComboBox<String> sortOptionsComboBox;
    
    // OperationsManager instance
    private OperationsManager om = new OperationsManager();
  

    public FinanceAppGUI() {
        setTitle("Personal Finance App");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        initComponents();
        pack();
        setLocationRelativeTo(null);
        
    }

    private void initComponents() {
    	// Transaction type radio buttons
        positiveRadioButton = new JRadioButton("Positive");
        negativeRadioButton = new JRadioButton("Negative");
        ButtonGroup transactionTypeGroup = new ButtonGroup();
        transactionTypeGroup.add(positiveRadioButton);
        transactionTypeGroup.add(negativeRadioButton);
        
        // Text fields
        amountTextField = new JTextField(10);
        descriptionTextField = new JTextField(20);
        dayTextField = new JTextField(2);
        monthTextField = new JTextField(2);
        yearTextField = new JTextField(4);

        // Buttons
        addTransactionButton = new JButton("Add Transaction");
        exportButton = new JButton("Export");
        importButton = new JButton("Import");
        generateReportButton = new JButton("Generate Report");

        // Sort options combo box
        sortOptionsComboBox = new JComboBox<>();
        sortOptionsComboBox.addItem("Date");
        sortOptionsComboBox.addItem("Amount");
        
        // Set layout and constraints
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(5, 5, 5, 5);

        // Add components to the frame
        add(new JLabel("Transaction Type:"), gbc);
        gbc.gridy+=2;
        add(new JLabel("Amount:"), gbc);
        gbc.gridy++;
        add(new JLabel("Description:"), gbc);
        gbc.gridy++;
        add(new JLabel("Date (DD/MM/YYYY):"), gbc);
        gbc.gridy++;

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.EAST;
        add(positiveRadioButton, gbc);
        gbc.gridy++;
        add(negativeRadioButton, gbc);
        gbc.gridy++;
        add(amountTextField, gbc);
        gbc.gridy++;
        add(descriptionTextField, gbc);
        gbc.gridy++;
        JPanel datePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        datePanel.add(dayTextField);
        datePanel.add(new JLabel("/"));
        datePanel.add(monthTextField);
        datePanel.add(new JLabel("/"));
        datePanel.add(yearTextField);
        add(datePanel, gbc);
        gbc.gridy++;

        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(10, 5, 5, 5);
        add(addTransactionButton, gbc);
        
        gbc.gridy++;
        add(generateReportButton, gbc);
        
        gbc.gridy++;
        gbc.anchor = GridBagConstraints.WEST;
        add(new JLabel("Export Sort Options:"), gbc);
        gbc.anchor = GridBagConstraints.EAST;
        add(sortOptionsComboBox, gbc);
        
        gbc.gridy++;
        gbc.anchor = GridBagConstraints.CENTER;
        add(exportButton, gbc);
        
        gbc.gridy++;
        gbc.anchor = GridBagConstraints.CENTER;
        add(importButton, gbc);

        // Action Listeners
        addTransactionButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                addTransaction();
            }
        });

        generateReportButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	if (om.getTransactions().isEmpty()) {
        			JOptionPane.showMessageDialog(null, "No transactions available! ", "Error", JOptionPane.ERROR_MESSAGE);
        		} else {
        			String report = om.generateReport();
        			ReportWindow reportWindow = new ReportWindow();
        			reportWindow.displayReport(report);
        		}
            }
        });
        
        exportButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	if (om.getTransactions().isEmpty()) {
        			JOptionPane.showMessageDialog(null, "No transactions available! ", "Error", JOptionPane.ERROR_MESSAGE);
        		} else {
            	String sortOption = (String) sortOptionsComboBox.getSelectedItem();
                om.exportToFile(sortOption);
            }
            	}
        });
        
        importButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	importFromFile();
            }
        });
    }

    private void addTransaction() {
        try {
        	// Check if all fields are filled
        	if (amountTextField.getText().isEmpty() || descriptionTextField.getText().isEmpty() ||
        	        dayTextField.getText().isEmpty() || monthTextField.getText().isEmpty() || yearTextField.getText().isEmpty() ||
        	        (!positiveRadioButton.isSelected() && !negativeRadioButton.isSelected())) {
        	    throw new EmptyFieldException("All fields must be filled!");
        	}
        	
        	// Check if the type of transaction is selected
        	String type = positiveRadioButton.isSelected() ? "+" : "-";
        	double amount = Double.parseDouble(amountTextField.getText());
        	if(type.equals("-")){
        		amount = -amount;
        	}
        	
        	// Get the transaction components
        	String description = descriptionTextField.getText();
        	
        	Calendar calendar = Calendar.getInstance();
        	calendar.setLenient(false);
        
        	int day = Integer.parseInt(dayTextField.getText());
        	int month = Integer.parseInt(monthTextField.getText());
        	int year = Integer.parseInt(yearTextField.getText());
        	
        	calendar.set(Calendar.YEAR, year);
        	calendar.set(Calendar.MONTH, month - 1);
        	calendar.set(Calendar.DAY_OF_MONTH, day);
        	Date date = calendar.getTime();
        
        	om.addTransaction(type, amount, description, date);
        	
        	// Clear input fields
            amountTextField.setText("");
            descriptionTextField.setText("");
            dayTextField.setText("");
            monthTextField.setText("");
            yearTextField.setText("");
            
            // Handle errors
        } catch (EmptyFieldException e) {
        	JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        
        } catch (NumberFormatException e) {
        	JOptionPane.showMessageDialog(null, "Invalid value input!", "Error", JOptionPane.ERROR_MESSAGE);
        	
        } catch (IllegalArgumentException  e) {
        	JOptionPane.showMessageDialog(null, "Invalid Date!", "Error", JOptionPane.ERROR_MESSAGE);
        }
        
        
    }
    
    private void importFromFile() {
    	JFileChooser fileChooser = new JFileChooser();
    	
    	FileNameExtensionFilter filter = new FileNameExtensionFilter("Text files", "txt");
        fileChooser.setFileFilter(filter);
        
        int result = fileChooser.showOpenDialog(null);
        
        if (result == JFileChooser.APPROVE_OPTION) {
            // The user has chosen a file
            String filePath = fileChooser.getSelectedFile().getAbsolutePath();
            om.importFromFile(filePath);
        }
    }
    
    // Custom exception class for empty fields
    class EmptyFieldException extends Exception {
        public EmptyFieldException(String s)
        {
            super(s);
        }
    }
    
    
}

