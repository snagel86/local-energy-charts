package de.local.energycharts.infrastructure.solarcity.api.admin.model;

import lombok.Data;

import java.time.Instant;

@Data
public class SolarCityResponse {

  private String id;
  private String name;
  private String municipalityKey;
  private Instant created;
  private Instant updated;
  private Double entireSolarPotentialOnRooftopsMWp;
  private Integer targetYear;
}
