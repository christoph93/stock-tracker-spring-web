package cc.stock.tracker.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import cc.stock.tracker.document.Portfolio;

import java.util.List;
import java.util.Optional;


@Repository
public interface PortfolioRepository extends MongoRepository<Portfolio, String> {


    Optional<List<Portfolio>> findByOwner(String owner);

    @Query("{ 'owner' : ?0 , 'name' : ?1}")
    Optional<Portfolio> findByOwnerAndName(String owner, String name);

}
