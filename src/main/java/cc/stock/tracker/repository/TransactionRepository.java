package cc.stock.tracker.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import cc.stock.tracker.document.Transaction;

import java.util.List;
import java.util.Optional;

@Repository
public interface TransactionRepository extends MongoRepository<Transaction, String> {

	Optional<List<Transaction>> findBySymbol(String symbol);
	
	
//	@Query(fields= "{'symbol: 1'}")
//    List<Transaction> findAllSymbols();

}
