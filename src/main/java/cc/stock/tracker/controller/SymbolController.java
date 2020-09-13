package cc.stock.tracker.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import cc.stock.tracker.document.Symbol;
import cc.stock.tracker.repository.SymbolRepository;

@RestController
public class SymbolController {

	@Autowired
	private SymbolRepository symbolRepository;
	
	@GetMapping("/symbol")
	public Symbol getSymbol(String symbol) {		
		
		
		
		return symbolRepository.findBySymbol(symbol);
	}
	
	
}
