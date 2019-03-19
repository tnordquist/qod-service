package edu.cnm.deepdive.qod.controller;

import edu.cnm.deepdive.qod.model.dao.QuoteRepository;
import edu.cnm.deepdive.qod.model.dao.SourceRepository;
import edu.cnm.deepdive.qod.model.entity.Quote;
import edu.cnm.deepdive.qod.model.entity.Source;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import javax.print.attribute.standard.Media;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/sources/{sourceId}/quotes")
public class QuoteController {

  private SourceRepository sourceRepository;
  private QuoteRepository quoteRepository;

  @Autowired
  public QuoteController(SourceRepository sourceRepository, QuoteRepository quoteRepository) {
    this.sourceRepository = sourceRepository;
    this.quoteRepository = quoteRepository;
  }

  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  public List<Quote> list(@PathVariable("sourceId") UUID sourceId) {
    return sourceRepository.findById(sourceId).get().getQuotes();
  }

  @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  public Quote post(@PathVariable("sourceId") UUID sourceId, @RequestBody Quote quote) {
    Source source = sourceRepository.findById(sourceId).get();
    quote.setSource(source);
    quoteRepository.save(quote);
    return quote;
  }

  @ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Source not found")
  @ExceptionHandler(NoSuchElementException.class)
  public void notFound() {}

}




