package edu.cnm.deepdive.qod.controller;

import edu.cnm.deepdive.qod.model.dao.QuoteRepository;
import edu.cnm.deepdive.qod.model.entity.Quote;
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

  @Autowired
  public QuoteController(QuoteRepository quoteRepository) {
    this.quoteRepository = quoteRepository;
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

  @ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Quote not found")
  @ExceptionHandler(NoSuchElementException.class)
  public void notFound() {}

}
