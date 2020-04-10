package cc.stock.tracker.service;

import java.util.Date;
import java.util.TreeMap;

public interface AlphaVantage {
	
	public TreeMap<Date, Double> getClosginPrices(String symbol);

}
