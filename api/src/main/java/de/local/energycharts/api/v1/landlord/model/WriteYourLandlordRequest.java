package de.local.energycharts.api.v1.landlord.model;

import lombok.Data;

@Data
public class WriteYourLandlordRequest {

  private String fromTenant;
  private String toLandlord;
  private String message;
}
