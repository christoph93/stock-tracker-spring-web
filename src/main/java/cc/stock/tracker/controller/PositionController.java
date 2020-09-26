package cc.stock.tracker.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cc.stock.tracker.document.Position;
import cc.stock.tracker.repository.PositionRepository;
import java.util.List;

@CrossOrigin
@RestController
public class PositionController {

	@Autowired
	private PositionRepository positionRepository;
	
	@GetMapping("/position")
	public Position getSymbol(@RequestParam(value="symbol", defaultValue="Test") String symbol) {
		return positionRepository.findBySymbol(symbol);				
	}
	
	
	@GetMapping("/allPositions")
	public List<Position> getAllPositions(){
		return positionRepository.findAll();
	}
	
	
}
