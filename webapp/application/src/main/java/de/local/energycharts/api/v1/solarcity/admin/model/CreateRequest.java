package de.local.energycharts.api.v1.solarcity.admin.model;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;


@Data
public class CreateRequest {

  @NotBlank
  private String cityName;
  private String municipalityKey;
  private Double entireSolarPotentialOnRooftopsMWp;
  private Integer targetYear;
}
