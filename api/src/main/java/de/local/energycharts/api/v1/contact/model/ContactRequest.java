package de.local.energycharts.api.v1.contact.model;

import lombok.Data;

@Data
public class ContactRequest {

  private String name;
  private String email;
  private String message;
}
