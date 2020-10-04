package cc.stock.tracker.service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
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
public class PositionUpdaterImpl {

	@Autowired
	private PositionRepository positionRepository;

	@Autowired
	private TransactionRepository transactionRepository;

	@Autowired
	private AliasRepository aliasRepository;

	@Autowired
	private DividendRepository dividendRepository;

	@Autowired
	private SymbolRepository symbolRepository;

	@Async
	public CompletableFuture<Position> update(Position position) {
		// this.position = positionArg;
		List<Transaction> transactionList = new ArrayList<Transaction>();
		List<Dividend> dividendList = new ArrayList<Dividend>();
		String positionAlias = position.getSymbol();
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
			transactionList.addAll(transactionRepository.findBySymbolAndUserId(s, position.getUserId()));
			dividendList.addAll(dividendRepository.findBySymbolAndUserId(s, position.getUserId()));
		});

//		this.position.setTransactions(transactionList);
//		this.position.setDividends(dividendList);

		updateAverages(position, transactionList);

		updateDividends(position, dividendList);

		updateCurrentPosition(position);

		positionRepository.save(position);
		System.out.println("Updated position: " + positionRepository.findById(position.getId()).get().getSymbol());
		return CompletableFuture.completedFuture(position);

	}

	private void updateDividends(Position position, List<Dividend> dividendList) {
		double total = dividendList.stream().reduce(0.0, (subtotal, e) -> subtotal + e.getNetValue(), Double::sum);
		position.setTotalDividends(total);
		position.setDividendCount(dividendList.size());
	}

	private void updateAverages(Position position, List<Transaction> transactionList) {
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

		position.setAvgBuyPrice(avgBuyPrice);
		position.setAvgSellPrice(avgSellPrice);
		position.setTotalPositionBought(totalPositionBought);
		position.setTotalPositionSold(totalPositionSold);
		position.setTotalUnitsBought(totalUnitsBought);
		position.setTotalUnitsSold(totalUnitsSold);
	}

	private void updateCurrentPosition(Position position) {

		Symbol symbol = symbolRepository.findBySymbol(position.getSymbol());

		if (symbol == null) {
			System.out.println("No symbols found for " + position.getSymbol());
			return;
		}

		position.setCurrentPrice(symbol.getLastPrice());
		position.setLastUpdateDate(symbol.getLastPriceDate());

		double currentOwnedUnits = position.getTotalUnitsBought() - position.getTotalUnitsSold();
		position.setCurrentOwnedUnits(currentOwnedUnits);

		double trades = 0;

		/*
		 * 
		 * trades, tradesPercent, , , currentOwnedUnits,
		 * 
		 */

		// position
		double auxPosition = currentOwnedUnits * position.getCurrentPrice();
		position.setPosition(auxPosition);

		// openResult
		position.setOpenResult((position.getCurrentOwnedUnits() * position.getCurrentPrice())
				- (position.getCurrentOwnedUnits() * position.getAvgBuyPrice()));

		// openResultPercent
		if (currentOwnedUnits > 0) {
			position.setOpenResultPercent(
					(position.getOpenResult() / (position.getCurrentOwnedUnits() * position.getAvgBuyPrice())) * 100);
		} else {
			position.setOpenResultPercent(0);
		}

		trades = (position.getAvgSellPrice() - position.getAvgBuyPrice()) * position.getTotalUnitsSold();

		position.setTrades(trades);

		if (position.getAvgSellPrice() != 0) {
			position.setTradesPercent(
					(position.getAvgSellPrice() - position.getAvgBuyPrice()) / position.getAvgBuyPrice());
		} else {
			position.setTradesPercent(0);
		}

//			this.position.setResult((this.position.getTotalUnitsSold() * this.position.getAvgSellPrice())
//					- (this.position.getTotalUnitsSold() * this.position.getAvgBuyPrice())
//					+ (( this.position.getAvgBuyPrice() * this.position.getCurrentOwnedUnits() - this.position.getOpenPosition()) ) );

		position.setLastUpdateDate(Date.from(Instant.now()));

	}

}
