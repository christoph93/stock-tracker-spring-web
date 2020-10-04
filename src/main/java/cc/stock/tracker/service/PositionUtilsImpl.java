package cc.stock.tracker.service;

import java.util.Date;
import java.util.HashSet;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.scheduling.annotation.EnableAsync;
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
	private SymbolRepository symbolRepository;

	@Autowired
	private PositionRepository positionRepository;

	@Autowired
	private PositionUpdaterImpl poistionUpdater;

	public void updateAllPositions() {
		// TODO: get user list from auth0
		ArrayList<String> users = new ArrayList<String>();
		users.add("auth0-5f6cc420d0e0e00073c901f0");

		users.forEach(u -> updatePositions(u));

	}

	public Position update(Position pos) {
		CompletableFuture<Position> temp = poistionUpdater.update(pos);
		try {
			return temp.get();
			
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}
		return null;
	}

	public void updatePositions(String userId) {
		System.out.println("updatind positions for " + userId);
		HashSet<String> symbols = new HashSet<>();
		
		/*symbolRepository.findAll().forEach(s -> {
			symbols.add(s.getAlias());
		});*/

		HashSet<String> existingPositions = new HashSet<String>();

		List<Position> positionList = positionRepository.findByUserId(userId);
		ArrayList<Future<Position>> futPositionList = new ArrayList<Future<Position>>(positionList.size());

		for (Position pos : positionList) {
			existingPositions.add(pos.getSymbol());
			System.out.println("Adding " + pos.getSymbol() + " to futPositionList");
			futPositionList.add(poistionUpdater.update(pos));
		}
		
		//create missing positions
		

		// check if all Futures are done
		int i = 0;
		while (i < futPositionList.size() - 1) {
			if (futPositionList.get(i).isDone()) {
				try {
					System.out.println(futPositionList.get(i).get().getSymbol() + " is done!");
				} catch (InterruptedException | ExecutionException e) {
					e.printStackTrace();
				}
				i++;
			}
		}
	}

}
