package cc.stock.tracker.service;

import java.util.Date;
import java.util.HashSet;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import cc.stock.tracker.document.Alias;
import cc.stock.tracker.document.Position;
import cc.stock.tracker.document.Symbol;
import cc.stock.tracker.document.Transaction;
import cc.stock.tracker.repository.AliasRepository;
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

	private List<Transaction> transactionList;
	private Position position;

	/*
	 * Update oldest {count} positions. Create missing positions from symbols.
	 */
	public void updatePositions() {
		HashSet<String> symbols = new HashSet<>();
		symbolRepository.findAll().forEach(s -> {
			symbols.add(s.getAlias());
		});

		List<Position> positions = positionRepository.findAll();

		positions.forEach(p -> {
			symbols.remove(p.getSymbol());
			update(p);
		});

		symbols.forEach(s -> {
			System.out.println("Creating new position for " + s);
			positionRepository.save(new Position(s));
		});

	}

	/*
	 * update all fields based on transactions and current prices
	 */
	public void update(Position positionArg) {
		this.position = positionArg;
		this.transactionList = new ArrayList<Transaction>();
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
			transactionList.addAll(transactionRepository.findBySymbol(s));
		});

		updateAverages();

		updateCurrentPosition();

		positionRepository.save(this.position);

		System.out.println("Updated position: " + positionRepository.findById(this.position.getId()));

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
		List<Symbol> temp = symbolRepository.findByAlias(position.getSymbol());

		if (temp.isEmpty()) {
			System.out.println("No symbols found for " + position.getSymbol());
			return;
		}

		this.position.setCurrentPrice(temp.get(0).getLastPrice());
		this.position.setLastUpdateDate(temp.get(0).getLastPriceDate());

		double currentOwnedUnits = this.position.getTotalUnitsBought() - this.position.getTotalUnitsSold();

		double profitLoss;
		double profitLossPercent;
		
		/*
		 * TODO: review these formulas. Process dividends
		 */

		if (currentOwnedUnits > 0) {
			this.position.setState("Open");
			this.position.setCurrentOwnedUnits(currentOwnedUnits);
			this.position.setOpenPosition(currentOwnedUnits * temp.get(0).getLastPrice());
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
			this.position.setState("Closed");
			this.position.setCurrentOwnedUnits(currentOwnedUnits);
			profitLoss = this.position.getTotalPositionBought() - this.position.getTotalPositionSold();
			this.position.setClosedPosition(profitLoss);
			this.position.setResult(this.position.getTotalPositionSold() - this.position.getTotalPositionBought());

		}

		this.position.setLastUpdateDate(Date.from(Instant.now()));

	}

}
