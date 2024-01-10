package de.local.energycharts.api.v1.solarcity.create.model;

import lombok.Data;

@Data
public class SolarCityCreatedResponse {

  private String id;
  private String cityName;
  private Integer totalNumberOfSolarInstallations;
}
