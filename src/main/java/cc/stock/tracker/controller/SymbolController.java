package cc.stock.tracker.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;



import org.springframework.web.bind.annotation.RequestParam;

import cc.stock.tracker.document.Symbol;
import cc.stock.tracker.repository.SymbolRepository;

@RestController
public class SymbolController {

	@Autowired
	private SymbolRepository symbolRepository;
	
	@GetMapping("/symbol")
	public Symbol getSymbol(@RequestParam(value="symbol", defaultValue="Test") String symbol) {
		return symbolRepository.findBySymbol(symbol);				
	}
	
	
	@GetMapping("/allSymbols")
	public List<Symbol> getAllSymbols(){
		return symbolRepository.findAll();
	}
	
	
}
