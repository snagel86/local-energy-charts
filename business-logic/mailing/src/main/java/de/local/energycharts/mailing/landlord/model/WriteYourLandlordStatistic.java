package de.local.energycharts.mailing.landlord.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class WriteYourLandlordStatistic {

  private Long pageOpened;
  private Long mailSent;
}
