package cc.stock.tracker.document;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.Date;

@Document
public class Transaction {

	@Id
	private String id;
	private Date transactionDate, recordCreateDate;
	private String operation, userId, symbol, description;
	private double quantity, price, totalPrice;
	private boolean deleted;

	public Transaction(String userId, Date transactionDate, String operation, String symbol, String description,
			double quantity, double price, double totalPrice, Date recordCreateDate) {
		this.userId = userId;
		this.transactionDate = transactionDate;
		this.operation = operation;
		this.symbol = symbol;
		this.description = description;
		this.quantity = quantity;
		this.price = price;
		this.totalPrice = totalPrice;
		this.recordCreateDate = recordCreateDate;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getId() {
		return id;
	}

	public Date getRecordCreateDate() {
		return recordCreateDate;
	}

	public void setRecordCreateDate(Date recordCreateDate) {
		this.recordCreateDate = recordCreateDate;
	}

	public Date getTransactionDate() {
		return transactionDate;
	}

	public void setTransactionDate(Date transactionDate) {
		this.transactionDate = transactionDate;
	}

	public String getOperation() {
		return operation;
	}

	public void setOperation(String operation) {
		this.operation = operation;
	}

	public String getSymbol() {
		return symbol;
	}

	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public double getQuantity() {
		return quantity;
	}

	public void setQuantity(double quantity) {
		this.quantity = quantity;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public double getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(double totalPrice) {
		this.totalPrice = totalPrice;
	}

	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}
	
	public void delete() {
		this.deleted = true;
	}
	
	public void restore() {
		this.deleted = false;
	}

	public boolean isDeleted() {
		return this.deleted;
	}

	@Override
	public String toString() {
		return "Transaction{" + "id='" + id + '\'' + ", date='" + transactionDate + '\'' + ", operation='" + operation
				+ '\'' + ", code='" + symbol + '\'' + ", description='" + description + '\'' + ", quantity=" + quantity
				+ ", price=" + price + ", totalPrice=" + totalPrice + ", createDate=" + recordCreateDate + '}';
	}
}
