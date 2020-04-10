package cc.stock.tracker.repository;



import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import cc.stock.tracker.document.Symbol;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface SymbolRepository extends MongoRepository<Symbol, String>{

    List<Symbol> findBySymbol(String symbol);
    List<Symbol> findByAlias(String alias);
    List<Symbol> deleteBySymbol(String symbol);
    Optional<List<Symbol>>findByCreateDateBefore(Date date);
    Optional<List<Symbol>> findByCreateDateAfter(Date date);    

}
