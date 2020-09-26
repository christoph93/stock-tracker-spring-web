package cc.stock.tracker.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;



import org.springframework.web.bind.annotation.RequestParam;

import cc.stock.tracker.document.Transaction;
import cc.stock.tracker.repository.TransactionRepository;

@CrossOrigin
@RestController
public class TransactionController {

	@Autowired
	private TransactionRepository transactionRepository;
	
	@GetMapping("/transactionBySymbol")
	public List<Transaction> getTransactionBySymbol(@RequestParam(value="symbol") String symbol, @RequestParam(value="userId") String userId) {
		return transactionRepository.findBySymbolAndUserId(symbol, userId);
	}
	
	@GetMapping("/transactionByUser")
	public List<Transaction> getTransactionByUser(@RequestParam(value="userId") String userId) {		
		return transactionRepository.findByUserId(userId);				
	}
	
	
	@GetMapping("/allTransactions")
	public List<Transaction> getAllTransactions(){
		return transactionRepository.findAll();
	}
	
	
}
