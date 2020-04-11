package cc.stock.tracker.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import cc.stock.tracker.document.Transaction;

import java.util.List;

@Repository
public interface TransactionRepository extends MongoRepository<Transaction, String> {

	List<Transaction> findBySymbol(String symbol);
	
	
//	@Query(fields= "{'symbol: 1'}")
//    List<Transaction> findAllSymbols();

}
