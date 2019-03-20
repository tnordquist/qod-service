package edu.cnm.deepdive.qod.view;

import java.util.Date;
import java.util.UUID;

public interface FlatQuote {

  UUID getId();

  Date getCreated();

  String getText();

}
