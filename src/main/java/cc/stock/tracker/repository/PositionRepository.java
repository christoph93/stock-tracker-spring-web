package cc.stock.tracker.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import cc.stock.tracker.document.Position;

@Repository
public interface PositionRepository extends MongoRepository<Position, String> {

	public Page<Position> findAll(Pageable pageable);
	
	public List<Position> findByUserId(String userId);

	public Position findBySymbol(String symbol);
	public Position findBySymbolAndUserId(String symbol, String UserId);
	
	@Query(value="{ 'userId' : ?0 }", fields="{id : 1}")
	public List<Position> findPositionIdsByUserId(String userId);

}
