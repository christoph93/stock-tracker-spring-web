package cc.stock.tracker.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.stereotype.Repository;

import cc.stock.tracker.document.Alias;

import java.util.List;

@RestResource
@Repository
public interface AliasRepository extends MongoRepository<Alias, String> {

	List<Alias> findByAlias(String alias);

	List<Alias> findBySymbol(String symbol);

}
