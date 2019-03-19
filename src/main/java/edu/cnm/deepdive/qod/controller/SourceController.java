package edu.cnm.deepdive.qod.controller;

import edu.cnm.deepdive.qod.model.dao.SourceRepository;
import edu.cnm.deepdive.qod.model.entity.Source;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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
@RequestMapping("/sources")
public class SourceController {

  private SourceRepository sourceRepository;

  @Autowired
  public SourceController(SourceRepository sourceRepository) {
    this.sourceRepository = sourceRepository;
  }

  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  public List<Source> list() {
    return sourceRepository.findAllByOrderByNameAsc();
  }

  @GetMapping(value = "search", produces = MediaType.APPLICATION_JSON_VALUE)
  public List<Source> search(@RequestParam("q") String fragment) {
    return sourceRepository.findAllByNameContainingOrderByNameAsc(fragment);
  }

  @GetMapping(value = "{sourceId}", produces = MediaType.APPLICATION_JSON_VALUE)
  public Source get(@PathVariable("sourceId") UUID sourceId) {
    return sourceRepository.findById(sourceId).get();
  }

  @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(value = HttpStatus.CREATED)
  public Source post(@RequestBody Source source) {
    sourceRepository.save(source);
    return source;
  }

  @DeleteMapping(value = "{sourceId}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void delete(@PathVariable("sourceId") UUID sourceId) {
    sourceRepository.delete(get(sourceId));
  }

  @ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Source not found")
  @ExceptionHandler(NoSuchElementException.class)
  public void notFound() {}
  
}













