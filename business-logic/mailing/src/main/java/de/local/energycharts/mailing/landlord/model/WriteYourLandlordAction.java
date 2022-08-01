package de.local.energycharts.mailing.landlord.model;

import java.time.Instant;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;

@Data
@Accessors(chain = true)
public class WriteYourLandlordAction {

  @Id
  private String id;
  private Instant pageOpenedAt;
  private Instant mailSentAt;
}
