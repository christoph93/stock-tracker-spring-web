package cc.stock.tracker.document;



import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class Position {

	@Id
	private String id;
	

    private String symbol, portfolioId;
    public double avgBuyPrice, avgSellPrice, totalUnitsBought, totalUnitsSold, totalPositionBought, totalPositionSold, profitFromSales,
            profitPercent, openPositionValue, currentOwnedUnits, openPositionProfit, currentPrice, totalDividends = 0;
    transient List<Transaction> transactions;
    public String state;

    public Position(String symbol, String portfolioId) {
        this.symbol = symbol;
        this.portfolioId = portfolioId;
    }

    public double getProfitFromSales() {
        return profitFromSales;
    }

    public void setProfitFromSales(double profitFromSales) {
        this.profitFromSales = profitFromSales;
    }

    public double getProfitPercent() {
        return profitPercent;
    }

    public void setProfitPercent(double profitPercent) {
        this.profitPercent = profitPercent;
    }

    public String getPortfolioId() {
        return portfolioId;
    }

    public void setPortfolioId(String portfolioId) {
        this.portfolioId = portfolioId;
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

    @Override
    public String toString() {
        return "Position{" +
                "symbol='" + symbol + '\'' +
                ", portfolioId='" + portfolioId + '\'' +
                ", avgBuyPrice=" + avgBuyPrice +
                ", avgSellPrice=" + avgSellPrice +
                ", totalUnitsBought=" + totalUnitsBought +
                ", totalUnitsSold=" + totalUnitsSold +
                ", totalPositionBought=" + totalPositionBought +
                ", totalPositionSold=" + totalPositionSold +
                ", profitFromSales=" + profitFromSales +
                ", profitPercent=" + profitPercent +
                ", openPositionValue=" + openPositionValue +
                ", currentOwnedUnits=" + currentOwnedUnits +
                ", transactions=" + transactions +
                ", state='" + state + '\'' +
                '}';
    }
}
