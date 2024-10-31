package de.local.energycharts.api.v1.time.model;

import java.time.Instant;
import lombok.Data;

@Data
public class FreezeNowRequest {

  private Instant now;
}
