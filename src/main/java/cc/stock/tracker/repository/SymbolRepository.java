package cc.stock.tracker.repository;



import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import cc.stock.tracker.document.Symbol;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface SymbolRepository extends MongoRepository<Symbol, String>{

    Optional<Symbol> findBySymbol(String symbol);
    Optional<Symbol> findByAlias(String alias);
    List<Symbol> deleteBySymbol(String symbol);
    List<Symbol> findByCreateDateBefore(Date date);
    List<Symbol> findByCreateDateAfter(Date date);


}
