package cc.stock.tracker.service;

import java.util.Date;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cc.stock.tracker.document.Alias;
import cc.stock.tracker.document.Position;
import cc.stock.tracker.document.Symbol;
import cc.stock.tracker.document.Transaction;
import cc.stock.tracker.repository.AliasRepository;
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

	private List<Transaction> transactionList;
	private Position position;

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

		System.out.println(this.position.toString());

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

		System.out.println("HALP");

		double profitLoss;
		double profitLossPercent;

		if (currentOwnedUnits > 0) {
			this.position.setState("Open");
			this.position.setCurrentOwnedUnits(currentOwnedUnits);
			this.position.setOpenPosition(currentOwnedUnits * temp.get(0).getLastPrice());
			profitLoss = (this.position.getTotalUnitsSold() * this.position.getAvgSellPrice()
					- this.position.getTotalUnitsBought() * this.position.getAvgBuyPrice()
					+ (this.position.getCurrentPrice() * this.position.getCurrentOwnedUnits()));

			this.position.setProfitLossValue(profitLoss);

			profitLossPercent = profitLoss / this.position.getTotalPositionBought() * 100;

			this.position.setProfitLossPercent(profitLossPercent);

		} else {
			this.position.setState("Closed");
			this.position.setCurrentOwnedUnits(currentOwnedUnits);
			profitLoss = this.position.getTotalPositionBought() - this.position.getTotalPositionSold();
			this.position.setClosedPosition(profitLoss);

		}
		
		/*
		 * TODO: check issue with symbols {alias : {$in : ["RBED11", "EQPA3"] }} 
		 */

	}

}
