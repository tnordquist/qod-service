package edu.cnm.deepdive.qod.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import edu.cnm.deepdive.qod.view.FlatQuote;
import edu.cnm.deepdive.qod.view.FlatSource;
import java.net.URI;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import javax.annotation.PostConstruct;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OrderBy;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityLinks;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

@JsonIgnoreProperties(
    value = {"created", "quotes", "href"}, allowGetters = true, ignoreUnknown = true)
@Component
@Entity
public class Source implements FlatSource {

  private static EntityLinks entityLinks;

  @Id
  @GeneratedValue(generator = "uuid2")
  @GenericGenerator(name = "uuid2", strategy = "uuid2")
  @Column(name = "source_id", columnDefinition = "CHAR(16) FOR BIT DATA",
      nullable = false, updatable = false)
  private UUID id;

  @NonNull
  @CreationTimestamp
  @Temporal(TemporalType.TIMESTAMP)
  @Column(nullable = false, updatable = false)
  private Date created;

  @NonNull
  @Column(length = 1024, nullable = false, unique = true)
  private String name;

  @JsonSerialize(contentAs = FlatQuote.class)
  @ManyToMany(fetch = FetchType.LAZY, mappedBy = "sources",
      cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
  @OrderBy("text ASC")
  private Set<Quote> quotes = new LinkedHashSet<>();

  public UUID getId() {
    return id;
  }

  public Date getCreated() {
    return created;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Set<Quote> getQuotes() {
    return quotes;
  }

  public URI getHref() {
    return entityLinks.linkForSingleResource(Source.class, id).toUri();
  }

  @PostConstruct
  private void init() {
    String ignore = entityLinks.toString();
  }

  @Autowired
  private void setEntityLinks(EntityLinks entityLinks) {
    Source.entityLinks = entityLinks;
  }

  @Override
  public int hashCode() {
    return (name != null) ? name.toUpperCase().hashCode() : 0;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null || obj.getClass() != getClass()) {
      return false;
    }
    Source other = (Source) obj;
    return Objects.equals(name, other.name) || (name != null && name.equalsIgnoreCase(other.name));
  }

}
