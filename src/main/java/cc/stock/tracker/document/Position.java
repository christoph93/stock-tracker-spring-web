package cc.stock.tracker.document;

import java.util.Date;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class Position {

	@Id
	private String id;

	private String symbol;
	private double avgBuyPrice, avgSellPrice, totalUnitsBought, totalUnitsSold, totalPositionBought, totalPositionSold,
			result, resultPercent, profitLossFromSales, openPosition, currentOwnedUnits, closedPosition, currentPrice,
			totalDividends;
	private List<Transaction> transactions;
	private String state;
	private Date lastUpdateDate;

	public Position(String symbol) {
		this.symbol = symbol;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public double getOpenPosition() {
		return openPosition;
	}

	public void setOpenPosition(double openPosition) {
		this.openPosition = openPosition;
	}

	public double getCurrentOwnedUnits() {
		return currentOwnedUnits;
	}

	public void setCurrentOwnedUnits(double currentOwnedUnits) {
		this.currentOwnedUnits = currentOwnedUnits;
	}

	public double getClosedPosition() {
		return closedPosition;
	}

	public void setClosedPosition(double openPositionProfit) {
		this.closedPosition = openPositionProfit;
	}

	public double getCurrentPrice() {
		return currentPrice;
	}

	public void setCurrentPrice(double currentPrice) {
		this.currentPrice = currentPrice;
	}

	public double getTotalDividends() {
		return totalDividends;
	}

	public void setTotalDividends(double totalDividends) {
		this.totalDividends = totalDividends;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public Date getLastUpdateDate() {
		return lastUpdateDate;
	}

	public void setLastUpdateDate(Date lastUpdateDate) {
		this.lastUpdateDate = lastUpdateDate;
	}

	public double getResult() {
		return result;
	}

	public void setResult(double result) {
		this.result = result;
	}

	public double getResultPercent() {
		return resultPercent;
	}

	public void setResultPercent(double resultPercent) {
		this.resultPercent = resultPercent;
	}

	public double getTotalUnitsBought() {
		return totalUnitsBought;
	}

	public void setTotalUnitsBought(double totalUnitsBought) {
		this.totalUnitsBought = totalUnitsBought;
	}

	public double getTotalUnitsSold() {
		return totalUnitsSold;
	}

	public void setTotalUnitsSold(double totalUnitsSold) {
		this.totalUnitsSold = totalUnitsSold;
	}

	public double getTotalPositionBought() {
		return totalPositionBought;
	}

	public void setTotalPositionBought(double totalPositionBought) {
		this.totalPositionBought = totalPositionBought;
	}

	public double getTotalPositionSold() {
		return totalPositionSold;
	}

	public void setTotalPositionSold(double totalPositionSold) {
		this.totalPositionSold = totalPositionSold;
	}

	public String getSymbol() {
		return symbol;
	}

	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}

	public double getAvgBuyPrice() {
		return avgBuyPrice;
	}

	public void setAvgBuyPrice(double avgBuyPrice) {
		this.avgBuyPrice = avgBuyPrice;
	}

	public double getAvgSellPrice() {
		return avgSellPrice;
	}

	public void setAvgSellPrice(double avgSellPrice) {
		this.avgSellPrice = avgSellPrice;
	}

	public List<Transaction> getTransactions() {
		return transactions;
	}

	public void setTransactions(List<Transaction> transactions) {
		this.transactions = transactions;
	}

	public double getProfitLossFromSales() {
		return profitLossFromSales;
	}

	public void setProfitLossFromSales(double profitLossFromSales) {
		this.profitLossFromSales = profitLossFromSales;
	}

	@Override
	public String toString() {
		return "Position [id=" + id + ", symbol=" + symbol + ", avgBuyPrice=" + avgBuyPrice + ", avgSellPrice="
				+ avgSellPrice + ", totalUnitsBought=" + totalUnitsBought + ", totalUnitsSold=" + totalUnitsSold
				+ ", totalPositionBought=" + totalPositionBought + ", totalPositionSold=" + totalPositionSold
				+ ", result=" + result + ", resultPercent=" + resultPercent + ", profitLossFromSales="
				+ profitLossFromSales + ", openPosition=" + openPosition + ", currentOwnedUnits=" + currentOwnedUnits
				+ ", closedPosition=" + closedPosition + ", currentPrice=" + currentPrice + ", totalDividends="
				+ totalDividends + ", state=" + state + ", lastUpdateDate=" + lastUpdateDate + "]";
	}
}
