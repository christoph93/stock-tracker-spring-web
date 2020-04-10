package cc.stock.tracker;

import javax.annotation.PostConstruct;

import org.apache.el.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import cc.stock.tracker.service.ExcelUtilsImpl;
import cc.stock.tracker.service.SymbolUtils;
import cc.stock.tracker.service.SymbolUtilsImpl;

@SpringBootApplication
@EnableScheduling
public class StockTrackerApplication {
	
	@Autowired
	private ExcelUtilsImpl excelUtilsImpl;
	
	@Autowired
	private SymbolUtilsImpl symbolUtilsImpl;

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
	
	
	@Scheduled(fixedDelay = 60000, initialDelay = 5000)
	public void updateAliases() {
//		symbolUtilsImpl.updateSymbolAliases();
		symbolUtilsImpl.createSymbolsFromTransactions();
		
	}
	
	

}
