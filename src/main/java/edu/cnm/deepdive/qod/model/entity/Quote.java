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
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
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

@Entity
@JsonIgnoreProperties(
    value = {"created", "sources", "href"}, allowGetters = true, ignoreUnknown = true)
@Component
public class Quote implements FlatQuote {

  private static EntityLinks entityLinks;

  @Id
  @GeneratedValue(generator = "uuid2")
  @GenericGenerator(name = "uuid2", strategy = "uuid2")
  @Column(name = "quote_id", columnDefinition = "CHAR(16) FOR BIT DATA",
      nullable = false, updatable = false)
  private UUID id;

  @NonNull
  @CreationTimestamp
  @Temporal(TemporalType.TIMESTAMP)
  @Column(nullable = false, updatable = false)
  private Date created;

  @NonNull
  @Column(length = 4096, nullable = false, unique = true)
  private String text;

  @JsonSerialize(contentAs = FlatSource.class)
  @ManyToMany(fetch = FetchType.LAZY,
      cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
  @JoinTable(joinColumns = @JoinColumn(name = "quote_id"),
      inverseJoinColumns = @JoinColumn(name = "source_id"))
  @OrderBy("name ASC")
  private Set<Source> sources = new LinkedHashSet<>();

  public UUID getId() {
    return id;
  }

  public Date getCreated() {
    return created;
  }

  public String getText() {
    return text;
  }

  public void setText(String text) {
    this.text = text;
  }

  public Set<Source> getSources() {
    return sources;
  }

  public URI getHref() {
    return entityLinks.linkForSingleResource(Quote.class, id).toUri();
  }

  @PostConstruct
  private void init() {
    String ignore = entityLinks.toString();
  }

  @Autowired
  private void setEntityLinks(EntityLinks entityLinks) {
    Quote.entityLinks = entityLinks;
  }

  @Override
  public int hashCode() {
    return (text != null) ? text.toUpperCase().hashCode() : 0;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null || obj.getClass() != getClass()) {
      return false;
    }
    Quote other = (Quote) obj;
    return Objects.equals(text, other.text) || (text != null && text.equalsIgnoreCase(other.text));
  }

}
