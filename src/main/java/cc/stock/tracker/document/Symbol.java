package cc.stock.tracker.document;



import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document
public class Symbol {

    @Id
    private String id;

    transient private Object fullContent;
    transient private Object closingPrices;
    private Date createDate;
    private String symbol, alias;
    private double lastPrice;
    private Date lastPriceDate;

    public Symbol(Object fullContent, Date createDate, String symbol) {
        this.fullContent = fullContent;
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

    public Object getClosingPrices() {
        return closingPrices;
    }

    public void setClosingPrices(Object closingPrices) {
        this.closingPrices = closingPrices;
    }

    public Object getFullContent() {
        return fullContent;
    }

    public void setFullContent(Object fullContent) {
        this.fullContent = fullContent;
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
        return "Symbol{" +
                "id='" + id + '\'' +
                ", content='" + fullContent + '\'' +
                ", createDate=" + createDate +
                ", symbol='" + symbol + '\'' +
                '}';
    }
}
