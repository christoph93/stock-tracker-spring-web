package cc.stock.tracker;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import cc.stock.tracker.service.ExcelUtilsImpl;
import cc.stock.tracker.service.PositionUtilsImpl;
import cc.stock.tracker.service.SymbolUtilsImpl;

@SpringBootApplication
@EnableScheduling
public class StockTrackerApplication {

	@Autowired
	private ExcelUtilsImpl excelUtilsImpl;

	@Autowired
	private SymbolUtilsImpl symbolUtilsImpl;

	@Autowired
	private PositionUtilsImpl positionUtilsImpl;

	public static void main(String[] args) {
		SpringApplication.run(StockTrackerApplication.class, args);
	}

	@PostConstruct
	public void init() {
		System.out.println("Saving transactions to mongo");
		saveTransactionsToMongo();

//		System.out.println("Saving dividends to mongo");
//		saveDividendsToMongo();
//
//		System.out.println("Creating symbols from transactions");
//		symbolUtilsImpl.createMissingSymbolsFromTransactions();
//
//		System.out.println("Updating aliases");
//		symbolUtilsImpl.updateSymbolAliases();
	}
	

//	@Scheduled(fixedDelay = 120000, initialDelay = 120000)
//	public void updateAliases() {
//		System.out.println("Schedule: updating aliases");
//		symbolUtilsImpl.updateSymbolAliases();
//	}
//
//	@Scheduled(fixedDelay = 65000, initialDelay = 10000)
//	public void updateClosingPrices() {
//		System.out.println("Schedule: updating closing prices (5)");
//		symbolUtilsImpl.updateClosingPrices(5);
//	}
//
//	@Scheduled(fixedDelay = 120000, initialDelay = 20000)
//	public void updatePositions() {
//		System.out.println("Schedule: updating positions");
//		positionUtilsImpl.updatePositions();
//	}

	public void saveTransactionsToMongo() {
		try {
			excelUtilsImpl.saveTransactionsToMongo("transactions.xls");
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
	}

	public void saveDividendsToMongo() {
		excelUtilsImpl.saveDividendsToMongo("./dividends.xls");
	}

}
