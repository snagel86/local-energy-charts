package de.local.energycharts.web.api.solarcity.admin.model;

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
