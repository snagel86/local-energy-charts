package de.local.energycharts.infrastructure.solarcity.api.admin.model;

import lombok.Data;

@Data
public class CreatedResponse {

  private String id;
  private String cityName;
  private Integer totalNumberOfSolarInstallations;
}
