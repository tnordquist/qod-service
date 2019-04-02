package edu.cnm.deepdive.qod.controller;

import edu.cnm.deepdive.qod.model.dao.QuoteRepository;
import edu.cnm.deepdive.qod.model.dao.SourceRepository;
import edu.cnm.deepdive.qod.model.entity.Quote;
import edu.cnm.deepdive.qod.model.entity.Source;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.ExposesResourceFor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@ExposesResourceFor(Quote.class)
@RequestMapping("/quotes")
public class QuoteController {

  private QuoteRepository quoteRepository;
  private SourceRepository sourceRepository;

  @Autowired
  public QuoteController(QuoteRepository quoteRepository, SourceRepository sourceRepository) {
    this.quoteRepository = quoteRepository;
    this.sourceRepository = sourceRepository;
  }

  @GetMapping(value = "random", produces = MediaType.APPLICATION_JSON_VALUE)
  public Quote getRandom() {
    return quoteRepository.findRandom().get();
  }

  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  public List<Quote> get() {
    return quoteRepository.findAllByOrderByTextAsc();
  }

  @GetMapping(value = "search", produces = MediaType.APPLICATION_JSON_VALUE)
  public List<Quote> search(@RequestParam("q") String fragment) {
    return quoteRepository.findAllByTextContainingOrderByTextAsc(fragment);
  }

  @PostMapping(
      consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Quote> post(@RequestBody Quote quote) {
    quoteRepository.save(quote);
    return ResponseEntity.created(quote.getHref()).body(quote);
  }

  @GetMapping(value = "{quoteId}", produces = MediaType.APPLICATION_JSON_VALUE)
  public Quote get(@PathVariable("quoteId") UUID quoteId) {
    return quoteRepository.findById(quoteId).get();
  }

  @DeleteMapping(value = "{quoteId}")
  public void delete(@PathVariable("quoteId") UUID quoteId) {
    quoteRepository.delete(get(quoteId));
  }

  @PostMapping(value = "{quoteId}/sources",
      consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Quote> attach(
      @PathVariable("quoteId") UUID quoteId, @RequestBody Source source) {
    source = sourceRepository.findById(source.getId()).get();
    Quote quote = get(quoteId);
    quote.getSources().add(source);
    quoteRepository.save(quote);
    return ResponseEntity.ok(quote);
  }

  @GetMapping(value = "{quoteId}/sources/{sourceId}", produces = MediaType.APPLICATION_JSON_VALUE)
  public Source get(
      @PathVariable("quoteId") UUID quoteId, @PathVariable("sourceId") UUID sourceId) {
    Quote quote = get(quoteId);
    Source source = sourceRepository.findById(sourceId).get();
    if (!quote.getSources().contains(source)) {
      throw new NoSuchElementException();
    }
    return source;
  }

  @DeleteMapping(value = "{quoteId}/sources/{sourceId}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void detach(
      @PathVariable("quoteId") UUID quoteId, @PathVariable("sourceId") UUID sourceId) {
    Quote quote = get(quoteId);
    Source source = sourceRepository.findById(sourceId).get();
    if (!quote.getSources().contains(source)) {
      throw new NoSuchElementException();
    }
    quote.getSources().remove(source);
    quoteRepository.save(quote);
  }

  @ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Resource not found")
  @ExceptionHandler(NoSuchElementException.class)
  public void notFound() {}

}
