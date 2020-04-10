package cc.stock.tracker.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import cc.stock.tracker.document.Position;

@Repository
public interface PositionRepository extends MongoRepository<Position, String>{

}
