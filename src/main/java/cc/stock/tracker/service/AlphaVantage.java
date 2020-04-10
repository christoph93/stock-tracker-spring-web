package cc.stock.tracker.service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public interface AlphaVantage {
	
	public HashMap<String, Double> getClosginPrices(String symbol);

}
