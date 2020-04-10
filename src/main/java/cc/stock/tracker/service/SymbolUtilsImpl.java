package cc.stock.tracker.service;

import java.time.Instant;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cc.stock.tracker.document.Alias;
import cc.stock.tracker.document.Symbol;
import cc.stock.tracker.repository.AliasRepository;
import cc.stock.tracker.repository.SymbolRepository;
import cc.stock.tracker.repository.TransactionRepository;

@Service
public class SymbolUtilsImpl implements SymbolUtils {

	@Autowired
	private AliasRepository aliasRepository;

	@Autowired
	private SymbolRepository symbolRepository;
	
	@Autowired
	private TransactionRepository transactionRepository;

	public void updateSymbolAliases() {		

		List<Alias> aliases = aliasRepository.findAll();

		if (aliases.isEmpty()) {
			System.out.println("No aliases found!");
			return;
		}

		aliases.forEach(alias -> {
			List<Symbol> symbols = symbolRepository.findBySymbol(alias.getSymbol());
			if (symbols.isEmpty()) {
				System.out.println("No symbols found for Alias " + alias.getAlias());

			} else {
				symbols.forEach(symbol -> {
					symbol.setAlias(alias.getAlias());
					Symbol temp = symbolRepository.save(symbol);
					System.out.println(
							"Updated symbol alias for symbol " + symbol.getSymbol() + " to " + symbol.getAlias());					
				});
			}

		});
	}
	
	
	/*
	 * TODO: implement query to get only distinct symbols from transactions instead of 
	 * doing it in java
	 */
	public void createSymbolsFromTransactions() {
		HashSet<String> distinctSymbols = new HashSet<String>();
		transactionRepository.findAll().forEach(transaction -> distinctSymbols.add(transaction.getSymbol()));
		distinctSymbols.forEach(symbol -> {
			symbolRepository.save(new Symbol(null, Date.from(Instant.now()), symbol));
		});
		
		/*
		 * TODO: modify Symbol to only hold closing prices.
		 * 
		 */
		
		
	}
	
	
	

}
