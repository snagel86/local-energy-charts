package de.local.energycharts.infrastructure.solarcity.api.time.model;

import lombok.Data;

import java.time.Instant;

@Data
public class FreezeNowRequest {

  private Instant now;
}
