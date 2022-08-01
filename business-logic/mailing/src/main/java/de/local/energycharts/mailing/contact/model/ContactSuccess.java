package de.local.energycharts.mailing.contact.model;

import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder @AllArgsConstructor @NoArgsConstructor
public class ContactSuccess {

  private String sent;
  private Instant at;
  private String to;
}
