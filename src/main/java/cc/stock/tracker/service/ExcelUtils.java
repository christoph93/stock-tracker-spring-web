package cc.stock.tracker.service;

import java.util.List;

import org.apache.el.parser.ParseException;

import cc.stock.tracker.document.Transaction;

public interface ExcelUtils {
	
	public List<Transaction> saveTransactionsToMongo(String path, String user) throws ParseException;

}
