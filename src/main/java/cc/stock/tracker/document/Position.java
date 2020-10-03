package cc.stock.tracker.document;

import java.util.Date;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class Position {

	@Id
	private String id;

	private String symbol, userId;
	private double avgBuyPrice, avgSellPrice, totalUnitsBought, totalUnitsSold, totalPositionBought, totalPositionSold,
			trades, tradesPercent, position, openResult, openResultPercent, currentOwnedUnits, currentPrice,
			totalDividends, dividendCount;
	transient List<String> transactions;
	transient List<String> dividends;
	private boolean isDeleted;
	private Date lastUpdateDate;

	public Position(String userId, String symbol) {
		this.userId = userId;
		this.symbol = symbol;
	}

	public double getOpenResult() {
		return openResult;
	}

	public void setOpenResult(double openResult) {
		this.openResult = openResult;
	}

	public double getOpenResultPercent() {
		return openResultPercent;
	}

	public void setOpenResultPercent(double openResultPercent) {
		this.openResultPercent = openResultPercent;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public double getDividendCount() {
		return dividendCount;
	}

	public void setDividendCount(double dividendCount) {
		this.dividendCount = dividendCount;
	}

	public List<String> getDividends() {
		return dividends;
	}

	public void setDividends(List<String> dividends) {
		this.dividends = dividends;
	}

	public void setDeleted(boolean isDeleted) {
		this.isDeleted = isDeleted;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public double getPosition() {
		return position;
	}

	public void setPosition(double openPosition) {
		this.position = openPosition;
	}

	public double getCurrentOwnedUnits() {
		return currentOwnedUnits;
	}

	public void setCurrentOwnedUnits(double currentOwnedUnits) {
		this.currentOwnedUnits = currentOwnedUnits;
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

	public Date getLastUpdateDate() {
		return lastUpdateDate;
	}

	public void setLastUpdateDate(Date lastUpdateDate) {
		this.lastUpdateDate = lastUpdateDate;
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

	public List<String> getTransactions() {
		return transactions;
	}

	public void setTransactions(List<String> transactions) {
		this.transactions = transactions;
	}

	public double getTrades() {
		return trades;
	}

	public void setTrades(double profitLossFromSales) {
		this.trades = profitLossFromSales;
	}

	public double getTradesPercent() {
		return tradesPercent;
	}

	public void setTradesPercent(double tradesPercent) {
		this.tradesPercent = tradesPercent;
	}

	public void delete() {
		this.isDeleted = true;
	}

	public void restore() {
		this.isDeleted = false;
	}

	public boolean isDeleted() {
		return this.isDeleted;
	}

}
