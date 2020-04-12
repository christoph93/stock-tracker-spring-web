package cc.stock.tracker.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
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
		List<Symbol> symbolList = symbolRepository.findBySymbol(symbol);
		return (symbolList.isEmpty() ? symbol : symbolRepository.findBySymbol(symbol).get(0).getAlias());
	}

	public void updateSymbolAliases() {

		List<Alias> aliases = aliasRepository.findAll();

		if (aliases.isEmpty()) {
			System.out.println("No aliases found!");
			return;
		}

		aliases.forEach(alias -> {
			List<Symbol> symbols = symbolRepository.findBySymbol(alias.getSymbol());
			if (symbols.isEmpty()) {
				System.out.println("No symbols found for Alias " + alias.getAlias() + ". Creating...");
				symbolRepository.save(new Symbol(null, Date.from(Instant.now()), alias.getSymbol(), alias.getAlias()));

			} else {
				symbols.forEach(symbol -> {
					symbol.setAlias(alias.getAlias());
					System.out.println(
							"Updated symbol alias for symbol " + symbol.getSymbol() + " to " + symbol.getAlias());
				});
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

		distinctSymbolsFromTransactions.forEach(s -> {
			aliases.addAll(aliasRepository.findBySymbol(s));
		});

		System.out.println("Aliases for above symbols: ");
		aliases.forEach(a -> System.out.println(a.getSymbol() + " -> " + a.getAlias()));

		aliases.forEach(a -> {
			distinctSymbolsFromTransactions.remove(a.getSymbol());
			distinctSymbolsFromTransactions.add(a.getAlias());
		});

		System.out.println(distinctSymbolsFromTransactions);

		List<Symbol> existingSymbols = new ArrayList<Symbol>();

		distinctSymbolsFromTransactions.forEach(s -> {
			existingSymbols.addAll(symbolRepository.findByAlias(s));
		});

		existingSymbols.forEach(s -> {
			distinctSymbolsFromTransactions.remove(s.getSymbol());
		});

		distinctSymbolsFromTransactions.forEach(symbolString -> {
			System.out.println("Creating symbol: " + symbolString);
			symbolRepository.save(new Symbol(null, Date.from(Instant.now()), symbolString, symbolString));
		});
	}

	/*
	 * Update closing prices of the 'count' oldest symbols and return the ordered
	 * TreeMap of closing prices
	 */
	public HashMap<String, TreeMap<Date, Double>> updateOldestClosingPrices(int count) {
		HashMap<String, TreeMap<Date, Double>> symbolAndClosingPrices = new HashMap<String, TreeMap<Date, Double>>();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
		Page<Symbol> oldest = symbolRepository.findAll(PageRequest.of(0, count, Sort.Direction.ASC, "updateDate"));
		oldest.forEach(symbol -> {
			TreeMap<Date, Double> orderedClosingPrices = new TreeMap<>();
			Map<String, Double> closingPrices = alphaVantageImpl.getClosginPrices(symbol.getAlias());
			closingPrices.entrySet().forEach(entry -> {
				try {
					orderedClosingPrices.put(formatter.parse(entry.getKey()), entry.getValue());
				} catch (ParseException e) {
					e.printStackTrace();
				}
			});

			symbol.setClosingPrices(closingPrices);
			symbol.setUpdateDate(Date.from(Instant.now()));
			symbol.setLastPriceDate(orderedClosingPrices.lastEntry().getKey());
			symbol.setLastPrice(orderedClosingPrices.lastEntry().getValue());
			symbolRepository.deleteBySymbol(symbol.getSymbol());
			symbolRepository.save(symbol);
			symbolAndClosingPrices.put(symbol.getAlias(), orderedClosingPrices);
			System.out.println("Updated closing prices for symbol " + symbol.getSymbol());
		});
		return symbolAndClosingPrices;
	}

}
