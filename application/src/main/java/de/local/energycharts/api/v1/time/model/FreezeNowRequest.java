package de.local.energycharts.api.v1.time.model;

import lombok.Data;

import java.time.Instant;

@Data
public class FreezeNowRequest {

  private Instant now;
}
