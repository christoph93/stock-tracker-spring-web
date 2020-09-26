package cc.stock.tracker.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import cc.stock.tracker.document.Dividend;

import java.util.Date;
import java.util.List;
import java.util.Optional;


@Repository
public interface DividendRepository extends MongoRepository<Dividend, String>{


    Optional<List<Dividend>> findBySymbolAndUserId(String symbol, String userId);
    
    List<Dividend> findByUserId(String userId);

    @Query("{ 'symbol' : ?0 , 'payDate' : { $lt : ?1} }")
    Optional<List<Dividend>> findBySymbolAndPayDateBefore(String symbol, Date payDate);


    }
