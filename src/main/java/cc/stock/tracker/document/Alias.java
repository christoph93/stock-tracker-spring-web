package cc.stock.tracker.document;

import java.util.Date;

import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class Alias {


    private String symbol;
    private String alias;
    private Date createDate;

    public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Alias(String symbol, String alias) {
        this.symbol = symbol;
        this.alias = alias;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }
    
    
}
