package edu.cnm.deepdive.qod.view;

import java.util.Date;
import java.util.UUID;

public interface FlatSource {

  UUID getId();

  Date getCreated();

  String getName();

}
