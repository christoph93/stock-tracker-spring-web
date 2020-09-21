package cc.stock.tracker.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import cc.stock.tracker.document.Position;

@Repository
public interface PositionRepository extends MongoRepository<Position, String> {

	public Page<Position> findAll(Pageable pageable);

	public Position findBySymbol(String symbol);

}
