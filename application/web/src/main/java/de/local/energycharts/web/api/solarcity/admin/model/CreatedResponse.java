package de.local.energycharts.web.api.solarcity.admin.model;

import lombok.Data;

@Data
public class CreatedResponse {

  private String id;
  private String cityName;
  private Integer totalNumberOfSolarInstallations;
}
