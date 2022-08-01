package de.local.energycharts.mailing.landlord.model;

import lombok.Data;

@Data
public class WriteYourLandlord {

  private String fromTenant;
  private String toLandlord;
  private String message;
}
