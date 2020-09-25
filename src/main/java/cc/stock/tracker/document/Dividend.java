package cc.stock.tracker.document;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document
public class Dividend {

    @Id
    private String id;

    private String symbol, description, userID;
    private Date payDate;
    private double grossValue, taxValue, netValue;


    public Dividend(Date payDate, String description, String symbol, double grossValue, double taxValue, double netValue) {
        this.symbol = symbol;        
        this.description = description;
        this.payDate = payDate;
        this.grossValue = grossValue;
        this.taxValue = taxValue;
        this.netValue = netValue;
    }
    
    

    public String getUserID() {
		return userID;
	}

	public void setUserID(String userID) {
		this.userID = userID;
	}

	public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public Date getPayDate() {
        return payDate;
    }

    public void setPayDate(Date payDate) {
        this.payDate = payDate;
    }

    public double getGrossValue() {
        return grossValue;
    }

    public void setGrossValue(double grossValue) {
        this.grossValue = grossValue;
    }

    public double getTaxValue() {
        return taxValue;
    }

    public void setTaxValue(double taxValue) {
        this.taxValue = taxValue;
    }

    public double getNetValue() {
        return netValue;
    }

    public void setNetValue(double netValue) {
        this.netValue = netValue;
    }

    @Override
    public String toString() {
        return "Dividend{" +
                ", symbol='" + symbol + '\'' +
                ", description='" + description + '\'' +
                ", payDate=" + payDate +
                ", netValue=" + netValue +
                '}';
    }
}
