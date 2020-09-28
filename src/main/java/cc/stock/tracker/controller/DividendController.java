package cc.stock.tracker.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;



import org.springframework.web.bind.annotation.RequestParam;

import cc.stock.tracker.document.Dividend;
import cc.stock.tracker.repository.DividendRepository;

@CrossOrigin
@RestController
public class DividendController {

	@Autowired
	private DividendRepository dividendRepository;
	
	@GetMapping("/dividend")
	public List<Dividend> getBySymbol(@RequestParam(value="symbol") String symbol, @RequestParam(value="userId") String userId) {
		return dividendRepository.findBySymbolAndUserId(symbol, userId);
	}
	
	
	@GetMapping("/allDividendsByUser")
	public List<Dividend> getAllActiveSymbols(@RequestParam(value="userId") String userId){
		return dividendRepository.findByUserId(userId);
	}
	
	
}
