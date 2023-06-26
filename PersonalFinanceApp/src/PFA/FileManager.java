package PFA;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.swing.JOptionPane;

public class FileManager {
	
	/**
     * Exports a list of transactions to a file.
     *
     * @param transactions the list of transactions to export
     * @param filePath     the path of the file to export to
     */
    public static void exportTransactions(List<Transaction> transactions, String filePath) {
    	
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (Transaction transaction : transactions) {
                String line = transaction.toExportString();
                writer.write(line);
                writer.newLine();
            }
            
            JOptionPane.showMessageDialog(null, "Transactions exported successfully.");
        } catch (IOException e) {
        	JOptionPane.showMessageDialog(null, "Error exporting transactions: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Imports transactions from a file.
     *
     * @param filePath the path of the file to import from
     * @return a list of imported transactions
     */
    public static List<Transaction> importTransactions(String filePath) {
        List<Transaction> importedTransactions = new ArrayList<>();
        boolean importSuccessful = true;

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                // Parse each line and extract transaction data
                String[] parts = line.split("	");
                
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                Date date;
                try {
                	date = dateFormat.parse(parts[1]);
                } catch (ParseException e) {
                    JOptionPane.showMessageDialog(null, "Error in the date format: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    importSuccessful = false;
                    break;
                }
                
                double amount = Double.parseDouble(parts[0].trim());
                String description = parts[2].trim();
                String type;
                if(amount < 0) {
                	type = "-";
                } else {
                	type = "+";
                }

                // Create Transaction object and add to the list
                Transaction transaction = new Transaction(type, amount, description, date);
                importedTransactions.add(transaction);
            }
            
            if(importSuccessful) {
            	JOptionPane.showMessageDialog(null, "Transactions imported successfully.");
            }
            
        } catch (Exception e) {
        	JOptionPane.showMessageDialog(null, "Error importing transactions: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }

        return importedTransactions;
    }
        
}

