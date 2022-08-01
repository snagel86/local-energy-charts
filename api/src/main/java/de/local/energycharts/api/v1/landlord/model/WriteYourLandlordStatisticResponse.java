package de.local.energycharts.api.v1.landlord.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class WriteYourLandlordStatisticResponse {

  private Long pageOpened;
  private Long mailSent;
}
