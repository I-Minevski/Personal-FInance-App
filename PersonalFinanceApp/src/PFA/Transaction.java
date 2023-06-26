package PFA;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Transaction {
	
	private String type; // Type of transaction (+ for income, - for expense)
    private double amount; // Amount of the transaction
    private String description; // Description of the transaction
    private Date date; // Date of the transaction
	
	public Transaction() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Transaction(String type, double amount, String description, Date date) {
		super();
		this.type = type;
		this.amount = amount;
		this.description = description;
		this.date = date;
	}

	public String getType() {
		return type;
	}

	public double getAmount() {
		return amount;
	}

	public String getDescription() {
		return description;
	}
	
	public Date getDate() {
		return date;
	}
	
	// Return the transaction details formatted for exporting
	public String toExportString() {
		StringBuilder exportBuilder = new StringBuilder();
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		String formattedDate = dateFormat.format(this.getDate());
		
		exportBuilder.append(this.amount);
		exportBuilder.append("	");
		exportBuilder.append(formattedDate);
		exportBuilder.append("	");
		exportBuilder.append(this.description);
		
		return exportBuilder.toString();
	}
	

	
}


