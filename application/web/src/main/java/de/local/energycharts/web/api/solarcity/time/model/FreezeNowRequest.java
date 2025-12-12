package de.local.energycharts.web.api.solarcity.time.model;

import lombok.Data;

import java.time.Instant;

@Data
public class FreezeNowRequest {

  private Instant now;
}
