package de.local.energycharts.api.v1.solarcity.admin.model;

import lombok.Data;

@Data
public class SolarCityCreatedResponse {

  private String id;
  private String cityName;
  private Integer totalNumberOfSolarInstallations;
}
