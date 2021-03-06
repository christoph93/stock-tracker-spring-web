package cc.stock.tracker.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cc.stock.tracker.document.Position;
import cc.stock.tracker.repository.PositionRepository;
import cc.stock.tracker.service.PositionUtilsImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

@CrossOrigin
@RestController
public class PositionController {

	@Autowired
	private PositionRepository positionRepository;

	@Autowired
	private PositionUtilsImpl positionUtils;

	@GetMapping("/positionBySymbol")
	public Position getBySymbol(@RequestParam(value = "symbol") String symbol,
			@RequestParam(value = "userId") String userId) {
		return positionRepository.findBySymbolAndUserId(symbol, userId);
	}

	@GetMapping("/positionsByUserId")
	public List<Position> getByUserId(@RequestParam(value = "userId") String userId) {
		positionUtils.updatePositions(userId);
		return positionRepository.findByUserId(userId);
	}

	@GetMapping("/positionIdsByUserId")
	public List<String> getPositionIdsByUserId(@RequestParam(value = "userId") String userId) {
		List<String> ids = new ArrayList<String>();
		positionRepository.findPositionIdsByUserId(userId).forEach(e -> {
			ids.add(e.getId());
		});

		return ids;
	}

	@GetMapping("/positionById")
	public Position getPositionById(@RequestParam(value = "id") String id) {
		System.out.println("finding position " + id);
		Optional<Position> opt = positionRepository.findById(id);
		if (opt.isPresent()) {
			return positionUtils.update(opt.get());
		}
		return null;

	}

	@GetMapping("/allPositions")
	public List<Position> getAllPositions() {
		return positionRepository.findAll();
	}

}
