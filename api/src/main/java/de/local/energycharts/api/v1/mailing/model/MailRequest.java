package de.local.energycharts.api.v1.mailing.model;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class MailRequest {

  @NotBlank
  private String subject;
  @NotBlank
  private String from;
  private String to;
  private String cc;
  @NotBlank
  private String message;
}
