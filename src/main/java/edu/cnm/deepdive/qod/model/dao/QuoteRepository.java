package edu.cnm.deepdive.qod.model.dao;

import edu.cnm.deepdive.qod.model.entity.Quote;
import edu.cnm.deepdive.qod.model.entity.Source;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface QuoteRepository extends CrudRepository<Quote, UUID> {

  Optional<Quote> findBySourceAndId(Source source, UUID quoteId);

  @Query(value = "SELECT * FROM quote ORDER BY RANDOM() OFFSET 0 ROWS FETCH NEXT 1 ROW ONLY",
      nativeQuery = true)
  Optional<Quote> findRandom();

  List<Quote> findAllByTextContainingOrderByTextAsc(String fragment);

}
