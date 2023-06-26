package PFA;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.swing.JOptionPane;

public class OperationsManager {
	
	private List<Transaction> transactions = new ArrayList<>(); // List to store all transactions
    
    private HashMap<String, List<Transaction>> incomeExpenseMap = new HashMap<>(); // HashMap to categorize transactions by income and expense
	
	public OperationsManager() {
		super();
		incomeExpenseMap.put("+", new ArrayList<Transaction>()); // Initialize the income list
        incomeExpenseMap.put("-", new ArrayList<Transaction>()); // Initialize the expense list
	}
	
	public void addTransaction(String type, double amount, String description, Date date) {
        Transaction newTransaction = new Transaction(type, amount, description, date);
        
        this.getTransactions().add(newTransaction); // Add transaction to the main list
        
        List<Transaction> temp_transactions = this.incomeExpenseMap.get(type);
        temp_transactions.add(newTransaction); // Add transaction to the corresponding income/expense list
        this.incomeExpenseMap.put(type, temp_transactions);
    }
	
	public String generateReport() {
		
		Comparator<Transaction> comparator = new DateComparator(); // Comparator for sorting transactions by date
        SortingUtils.mergeSort(getTransactions(), comparator); // Sort transactions by date
		
		StringBuilder reportBuilder = new StringBuilder();
		
		reportBuilder.append("Total Income: ").append(calculate(incomeExpenseMap.get("+"))).append("\n"); // Calculate and append total income
        reportBuilder.append("Total Expenses: ").append(-calculate(incomeExpenseMap.get("-"))).append("\n"); // Calculate and append total expenses
        reportBuilder.append("Balance: ").append(calculate(getTransactions())).append("\n"); // Calculate and append balance
        
        reportBuilder.append("\nLast 10 transactions:\n");
        
        if(getTransactions().size() < 10) {
        	List<Transaction> reveresedTransactions = new ArrayList<>(getTransactions());
        	Collections.reverse(reveresedTransactions); // Reverse the list to get the last 10 transactions
        	for(Transaction transaction : reveresedTransactions) {
            	reportBuilder.append(transaction.toExportString()); // Append transaction details to the report
            	reportBuilder.append("\n");
            }
        } else {
        	int i = 0;
        	while(i < 10) {
        		Transaction transaction = getTransactions().get(getTransactions().size() - 1 - i);
        		reportBuilder.append(transaction.toExportString()); // Append transaction details to the report
        		reportBuilder.append("\n");
        		i += 1;
        }
       }
        return reportBuilder.toString(); // Return the generated report
    }
	
	// Calculate the sum of amounts in the transactions
	private double calculate(List<Transaction> transactions) {
		double sum = 0;
		for(int i = 0; i < transactions.size(); i++)
		    sum += transactions.get(i).getAmount();
		return sum;
    }
	
	public void exportToFile(String sortOption) {
		if(sortOption.equals("Date")) {
			Comparator<Transaction> comparator = new DateComparator(); // Comparator for sorting transactions by date
            SortingUtils.mergeSort(this.getTransactions(), comparator); // Sort transactions by date
			
		} else if(sortOption.equals("Amount")) {
			Comparator<Transaction> comparator = new AmountComparator(); // Comparator for sorting transactions by amount
            SortingUtils.mergeSort(this.getTransactions(), comparator); // Sort transactions by amount
		}
		
		String filePath = "transactions.txt";
		
		FileManager.exportTransactions(getTransactions(), filePath); // Export transactions to a file
    	
    }
	
	public void importFromFile(String filePath) {
		List<Transaction> importedTransactions = FileManager.importTransactions(filePath); // Import transactions from a file
		
		this.getTransactions().addAll(importedTransactions); // Add imported transactions to the main list
		
		List<Transaction> positiveTransactions = this.incomeExpenseMap.get("+");
		List<Transaction> negativeTransactions = this.incomeExpenseMap.get("-");
        
		for(Transaction transaction : importedTransactions) {
			if (transaction.getType().equals("+")) {
				positiveTransactions.add(transaction); // Add imported income transactions to the income list
			} else {
				negativeTransactions.add(transaction); // Add imported expense transactions to the expense list
			}
		}
		
		this.incomeExpenseMap.put("+", positiveTransactions);
		this.incomeExpenseMap.put("-", negativeTransactions);
    	
    }

	public List<Transaction> getTransactions() {
		return transactions; // Return the list of transactions
	}
}
