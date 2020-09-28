package cc.stock.tracker.service;

import java.util.Date;
import java.util.HashSet;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cc.stock.tracker.document.Alias;
import cc.stock.tracker.document.Dividend;
import cc.stock.tracker.document.Position;
import cc.stock.tracker.document.Symbol;
import cc.stock.tracker.document.Transaction;
import cc.stock.tracker.repository.AliasRepository;
import cc.stock.tracker.repository.DividendRepository;
import cc.stock.tracker.repository.PositionRepository;
import cc.stock.tracker.repository.SymbolRepository;
import cc.stock.tracker.repository.TransactionRepository;

@Service
public class PositionUtilsImpl implements PositionUtils {

	@Autowired
	private TransactionRepository transactionRepository;

	@Autowired
	private AliasRepository aliasRepository;

	@Autowired
	private SymbolRepository symbolRepository;

	@Autowired
	private PositionRepository positionRepository;
	
	@Autowired
	private DividendRepository dividendRepository;

	private List<Transaction> transactionList;
	private Position position;
	private List<Dividend> dividendList;

	
	public void updateAllPositions() {
		//TODO: get user list from auth0
		ArrayList<String> users = new ArrayList<String>();
		users.add("auth0-5f6cc420d0e0e00073c901f0");
		
		
		users.forEach(u -> updatePositions(u));
		
		
	}
	
	public void updatePositions(String userId) {
		System.out.println("updatind positions for " + userId);
		HashSet<String> symbols = new HashSet<>();
		symbolRepository.findAll().forEach(s -> {
			symbols.add(s.getAlias());
		});

		List<Position> positions = positionRepository.findByUserId(userId);

		positions.forEach(p -> {
			symbols.remove(p.getSymbol());
			update(p);
		});

		symbols.forEach(s -> {
			System.out.println("Creating new position for " + s);
			positionRepository.save(new Position(userId, s));
		});

	}

	/*
	 * update all fields based on transactions, dividends and current prices
	 */
	public void update(Position positionArg) {
		this.position = positionArg;
		this.transactionList = new ArrayList<Transaction>();
		this.dividendList = new ArrayList<Dividend>();
		String positionAlias = this.position.getSymbol();
		List<Alias> aliasList = aliasRepository.findByAlias(positionAlias);
		List<String> symbolList = new ArrayList<String>();

		/*
		 * If the positions' symbol does not have any aliases, add the current symbol as
		 * alias; else find all symbols for alias and find corresponding transactions
		 */

		if (aliasList.isEmpty()) {
			symbolList.add(positionAlias);
		} else {
			aliasList.forEach(a -> symbolList.add(a.getSymbol()));
		}
		
		

		symbolList.forEach(s -> {
			transactionList.addAll(transactionRepository.findBySymbolAndUserId(s, this.position.getUserId()));			
			dividendList.addAll(dividendRepository.findBySymbolAndUserId(s, this.position.getUserId()));
		});
		
		this.position.setTransactions(transactionList);
		this.position.setDividends(dividendList);

		updateAverages();

		updateCurrentPosition();
		
		updateDividends();

		positionRepository.save(this.position);

		System.out.println("Updated position: " + positionRepository.findById(this.position.getId()));

	}
	
	private void updateDividends(){
		double total = dividendList.stream().reduce(0.0, (subtotal, e) -> subtotal + e.getNetValue(), Double::sum );
		this.position.setTotalDividends(total);
		this.position.setDividendCount(dividendList.size());		
	}

	private void updateAverages() {
		double totalPositionBought = 0, totalPositionSold = 0;
		double totalUnitsBought = 0, totalUnitsSold = 0;
		double avgBuyPrice = 0, avgSellPrice = 0;

		for (Transaction t : transactionList) {

			if (t.getOperation().equals("C")) {
				totalUnitsBought += t.getQuantity();
				totalPositionBought += t.getTotalPrice();
			} else if (t.getOperation().equals("V")) {
				totalUnitsSold += t.getQuantity();
				totalPositionSold += t.getTotalPrice();
			}
		}

		if (totalUnitsSold != 0) {
			avgSellPrice = totalPositionSold / totalUnitsSold;
		}

		avgBuyPrice = totalPositionBought / totalUnitsBought;

		this.position.setAvgBuyPrice(avgBuyPrice);
		this.position.setAvgSellPrice(avgSellPrice);
		this.position.setTotalPositionBought(totalPositionBought);
		this.position.setTotalPositionSold(totalPositionSold);
		this.position.setTotalUnitsBought(totalUnitsBought);
		this.position.setTotalUnitsSold(totalUnitsSold);
	}

	private void updateCurrentPosition() {		

		Symbol symbol = symbolRepository.findBySymbol(position.getSymbol());

		if (symbol == null) {
			System.out.println("No symbols found for " + position.getSymbol());
			return;
		}

		this.position.setCurrentPrice(symbol.getLastPrice());
		this.position.setLastUpdateDate(symbol.getLastPriceDate());

		double currentOwnedUnits = this.position.getTotalUnitsBought() - this.position.getTotalUnitsSold();

		double profitLoss;
		double profitLossPercent;
		
		/*
		 * TODO: review these formulas. Process dividends
		 */

		if (currentOwnedUnits > 0) {
			this.position.setOpen();
			this.position.setCurrentOwnedUnits(currentOwnedUnits);
			this.position.setOpenPosition(currentOwnedUnits * this.position.getCurrentPrice());
			profitLoss =
					// profit/loss from sales
					(position.getAvgBuyPrice() * position.getTotalUnitsSold()
							- position.getAvgSellPrice() * position.getTotalUnitsSold());

			this.position.setProfitLossFromSales(profitLoss);

			this.position.setResult(this.position.getOpenPosition() + position.getProfitLossFromSales()
					- (position.getTotalPositionBought() - position.getTotalPositionSold()));

//			this.position.setResult((this.position.getTotalUnitsSold() * this.position.getAvgSellPrice())
//					- (this.position.getTotalUnitsSold() * this.position.getAvgBuyPrice())
//					+ (( this.position.getAvgBuyPrice() * this.position.getCurrentOwnedUnits() - this.position.getOpenPosition()) ) );

			profitLossPercent = this.position.getResult() / this.position.getTotalPositionBought() * 100;

			this.position.setResultPercent(profitLossPercent);

		} else {
			this.position.setClosed();
			this.position.setCurrentOwnedUnits(currentOwnedUnits);
			profitLoss = this.position.getTotalPositionBought() - this.position.getTotalPositionSold();
			this.position.setClosedPosition(profitLoss);
			this.position.setResult(this.position.getTotalPositionSold() - this.position.getTotalPositionBought());

		}

		this.position.setLastUpdateDate(Date.from(Instant.now()));

	}

}
