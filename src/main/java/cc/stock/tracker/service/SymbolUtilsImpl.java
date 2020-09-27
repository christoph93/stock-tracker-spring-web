package cc.stock.tracker.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
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

	@Autowired
	private AlphaVantageImpl alphaVantageImpl;

	public String getAliasFromSymbol(String symbol) {
		Symbol auxSymbol = symbolRepository.findBySymbol(symbol);
		return (auxSymbol == null ? symbol : symbolRepository.findBySymbol(symbol).getAlias());
	}

	
	/*
	 * Get aliases and update corresponding symbols
	 */
	public void updateSymbolAliases() {
		List<Alias> aliases = aliasRepository.findAll();

		if (aliases.isEmpty()) {
			System.out.println("No aliases found!");
			return;
		}

		aliases.forEach(alias -> {
			Symbol symbol = symbolRepository.findBySymbol(alias.getSymbol());
			if (symbol == null) {
				System.out.println("No symbol found for alias " + alias.getAlias() + ". Creating...");
				symbolRepository.save(new Symbol(false,null, Date.from(Instant.now()), alias.getSymbol(), alias.getAlias()));
			} else {
				if(!symbol.getAlias().equals(alias.getAlias())) {
					symbol.setAlias(alias.getAlias());					
					if(symbol.getAlias().equals(symbol.getSymbol())) {
						symbol.setActive(true);
					} else {
						symbol.setActive(false);
					}
					symbolRepository.save(symbol);
					System.out.println("Updated alias for symbol " + symbol.getSymbol() + " to " + symbol.getAlias());
				}
			}

		});
	}

	/*
	 * Creates symbols based on transactions.
	 * 
	 * TODO: implement query to get only distinct symbols from transactions instead
	 * of doing it in java
	 */
	public void createMissingSymbolsFromTransactions() {
		HashSet<String> distinctSymbolsFromTransactions = new HashSet<String>();
		List<Alias> aliases = new ArrayList<Alias>();

		transactionRepository.findAll()
				.forEach(transaction -> distinctSymbolsFromTransactions.add(transaction.getSymbol()));

		System.out.println("Symbols from transactions: " + distinctSymbolsFromTransactions);

		//get aliases for these symbols
		distinctSymbolsFromTransactions.forEach(s -> {
			aliases.addAll(aliasRepository.findBySymbol(s));
		});

		System.out.println("Aliases for above symbols: ");
		aliases.forEach(a -> System.out.println(a.getSymbol() + " -> " + a.getAlias()));

		
		//replace the old symbol with the new alias
		aliases.forEach(a -> {
			distinctSymbolsFromTransactions.remove(a.getSymbol());
			distinctSymbolsFromTransactions.add(a.getAlias());
		});

		System.out.println(distinctSymbolsFromTransactions);

		List<Symbol> existingSymbols = new ArrayList<Symbol>();

		distinctSymbolsFromTransactions.forEach(s -> {
			existingSymbols.addAll(symbolRepository.findByAlias(s));
		});

		//remove symbols that already exist and keep only new ones
		existingSymbols.forEach(s -> {
			distinctSymbolsFromTransactions.remove(s.getSymbol());
		});

		distinctSymbolsFromTransactions.forEach(symbolString -> {
			System.out.println("Creating symbol: " + symbolString);
			symbolRepository.save(new Symbol(true, null, Date.from(Instant.now()), symbolString, symbolString));
		});
	}
	
	//update closing prices for the 'count' oldest symbols
	public void updateClosingPrices(int count){
		Page<Symbol> oldest = symbolRepository.findAll(PageRequest.of(0, count, Sort.Direction.ASC, "updateDate"));		
		oldest.forEach(symbol -> updateClosingPrices(symbol));		
	}
	
	
	public boolean updateClosingPrices(Symbol symbol) {
		Map<String, Double> stringClosingPrices = null;		
		try {
			stringClosingPrices = alphaVantageImpl.getClosginPrices(symbol.getAlias());
			TreeMap<Date, Double> dateClosingPrices = new TreeMap<Date, Double>();
			//convert strings into dates
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
			stringClosingPrices.entrySet().forEach(e -> {				
				try {
					dateClosingPrices.put(formatter.parse(e.getKey()), e.getValue());					
				} catch (ParseException e1) {
					e1.printStackTrace();
				}
			});
			symbol.setClosingPrices(stringClosingPrices);
			symbol.setUpdateDate(Date.from(Instant.now()));
			symbol.setLastPriceDate(dateClosingPrices.lastEntry().getKey());
			symbol.setLastPrice(dateClosingPrices.lastEntry().getValue());
			symbolRepository.save(symbol);
			System.out.println("Updated closing prices for symbol " + symbol.getSymbol());			
			return dateClosingPrices != null;
		} catch (Exception e){
			e.printStackTrace();
		}
		return false;
	}
	

}
