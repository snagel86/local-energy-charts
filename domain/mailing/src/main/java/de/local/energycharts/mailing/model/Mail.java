package de.local.energycharts.mailing.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Mail {

  private String subject;
  private String from;
  private String to;
  private String cc;
  private String message;
}
