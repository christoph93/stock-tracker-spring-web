package cc.stock.tracker.repository;



import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.CrossOrigin;

import cc.stock.tracker.document.Symbol;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@CrossOrigin
@Repository
public interface SymbolRepository extends MongoRepository<Symbol, String>{

    List<Symbol> findBySymbol(String symbol);
    List<Symbol> findByAlias(String alias);
    List<Symbol> deleteBySymbol(String symbol);
    Optional<List<Symbol>>findByUpdateDateBefore(Date updateDate);
    Optional<List<Symbol>> findByUpdateDateAfter(Date updateDate);
    
    Page<Symbol> findAll(Pageable pageable);

}
