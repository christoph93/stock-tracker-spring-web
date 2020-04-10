package cc.stock.tracker.document;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.TreeMap;

@Document
public class Symbol {

	@Id
	private String id;

	transient private TreeMap<Date, Double> closingPrices;
	private Date createDate;
	private String symbol, alias;
	private double lastPrice;
	private Date lastPriceDate;

	public Symbol(Object closingPrices, Date createDate, String symbol) {
		this.createDate = createDate;
		this.symbol = symbol;
		this.alias = symbol;
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

	public TreeMap<Date, Double> getClosingPrices() {
		return closingPrices;
	}

	public void setClosingPrices(TreeMap<Date, Double> closingPrices) {
		this.closingPrices = closingPrices;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
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

	@Override
	public String toString() {
		return "Symbol{" + "id='" + id + '\'' + '\'' + ", createDate=" + createDate + ", symbol='" + symbol + '\''
				+ '}';
	}
}
