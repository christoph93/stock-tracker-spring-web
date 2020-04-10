package cc.stock.tracker;

import javax.annotation.PostConstruct;

import org.apache.el.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import cc.stock.tracker.service.ExcelUtilsImpl;

@SpringBootApplication
public class StockTrackerApplication {
	
	@Autowired
	private ExcelUtilsImpl excelUtilsImpl;

	public static void main(String[] args) {
		SpringApplication.run(StockTrackerApplication.class, args);
	}
	
	
	@PostConstruct
	public void saveTransactionsToMongo() {
		try {
			excelUtilsImpl.saveTransactionsToMongo("./transactions.xls").forEach(line -> System.out.println(line.toString()));
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
	}

}
