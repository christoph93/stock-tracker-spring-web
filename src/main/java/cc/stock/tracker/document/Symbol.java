package cc.stock.tracker.document;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.Map;

@Document
public class Symbol {

	@Id
	private String id;

	transient private Map<String, Double> closingPrices;
	private Date updateDate;
	private boolean isActive;
	private String symbol, alias;
	private double lastPrice;
	private Date lastPriceDate;

	public Symbol(Map<String, Double> closingPrices, Date updateDate, String symbol, String alias) {
		this.updateDate = updateDate;
		this.symbol = symbol;
		this.alias = alias;
	}

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public String getId() {
		return id;
	}

	public Map<String, Double> getClosingPrices() {
		return closingPrices;
	}

	public void setClosingPrices(Map<String, Double> closingPrices) {
		this.closingPrices = closingPrices;
	}

	public Date getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	public String getSymbol() {
		return symbol;
	}

	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}

	public void setId(String id) {
		this.id = id;
	}

	public double getLastPrice() {
		return lastPrice;
	}

	public void setLastPrice(double lastPrice) {
		this.lastPrice = lastPrice;
	}

	public Date getLastPriceDate() {
		return lastPriceDate;
	}

	public void setLastPriceDate(Date lastPriceDate) {
		this.lastPriceDate = lastPriceDate;
	}
	
	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}
	
	public boolean isActive() {
		return isActive();
	}

	@Override
	public String toString() {
		return "Symbol{" + "id='" + id + '\'' + '\'' + ", updateDate=" + updateDate + ", symbol='" + symbol + '\''
				+ '}';
	}
}
