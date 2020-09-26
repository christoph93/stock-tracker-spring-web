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
	
	@GetMapping("/transaction")
	public List<Transaction> getTransactionBySymbol(@RequestParam(value="symbol", defaultValue="Test") String symbol) {
		return transactionRepository.findBySymbol(symbol);				
	}
	
	
	@GetMapping("/allTransactions")
	public List<Transaction> getAllTransactions(){
		return transactionRepository.findAll();
	}
	
	
}
