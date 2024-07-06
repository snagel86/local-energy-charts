package de.local.energycharts.api.v1.solarcity.create.model;

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
