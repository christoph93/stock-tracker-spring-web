package cc.stock.tracker.repository;


import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import cc.stock.tracker.document.Alias;

import java.util.List;
import java.util.Optional;

@Repository
public interface AliasRepository extends MongoRepository<Alias, String>{


    Optional<List<Alias>> findByAlias(String alias);
    Optional<List<Alias>> findBySymbol(String symbol);


    }
