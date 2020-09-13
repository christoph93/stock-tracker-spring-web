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

	//Symbol is unique
    Symbol findBySymbol(String symbol);
    
    //multiple symbols can exist with the same alias (multiple changes mean that 1 or more old symbols can point to the current alias) 
    List<Symbol> findByAlias(String alias);
    Symbol deleteBySymbol(String symbol);
    Optional<List<Symbol>>findByUpdateDateBefore(Date updateDate);
    Optional<List<Symbol>> findByUpdateDateAfter(Date updateDate);
    
    Page<Symbol> findAll(Pageable pageable);

}
