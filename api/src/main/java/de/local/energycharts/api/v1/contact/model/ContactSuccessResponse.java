package de.local.energycharts.api.v1.contact.model;

import java.time.Instant;
import lombok.Data;

@Data
public class ContactSuccessResponse {

  private String sent;
  private Instant at;
  private String to;
}
