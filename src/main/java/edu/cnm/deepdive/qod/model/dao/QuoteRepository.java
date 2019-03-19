package edu.cnm.deepdive.qod.model.dao;

import edu.cnm.deepdive.qod.model.entity.Quote;
import java.util.UUID;
import org.springframework.data.repository.CrudRepository;

public interface QuoteRepository extends CrudRepository<Quote, UUID> {

}
