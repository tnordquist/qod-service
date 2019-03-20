package edu.cnm.deepdive.qod.controller;

import edu.cnm.deepdive.qod.model.dao.QuoteRepository;
import edu.cnm.deepdive.qod.model.entity.Quote;
import java.util.NoSuchElementException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
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

  // TODO Add method for searching by fragment.

  @ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Quote not found")
  @ExceptionHandler(NoSuchElementException.class)
  public void notFound() {}

}
