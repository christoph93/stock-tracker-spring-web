package cc.stock.tracker;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import cc.stock.tracker.service.ExcelUtils;
import cc.stock.tracker.service.ExcelUtilsImpl;

@SpringBootApplication
public class StockTrackerApplication {
	
	@Autowired
	private ExcelUtilsImpl excelUtilsImpl;

	public static void main(String[] args) {
		SpringApplication.run(StockTrackerApplication.class, args);
	}
	
	
	@PostConstruct
	public void testExcel() {
		excelUtilsImpl.findTableInFile("./transactions.xls");
	}

}
