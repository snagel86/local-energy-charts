package de.local.energycharts.api.v1.highcharts.model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import lombok.Data;

@Data
public class AdditionOfSolarInstallationsResponse {

  private String id;
  private String name;
  private String drilldown;
  private Double y; // totalInstalledGrossPower
  private Integer numberOfSolarSystems;
  private Long numberOfSolarSystemsUpTo10kWp;
  private Long numberOfSolarSystems10To40kWp;
  private Long numberOfSolarSystemsFrom40kWp;
  private String color;

  public Double getY() {
    return BigDecimal.valueOf(y).setScale(2, RoundingMode.HALF_UP).doubleValue();
  }
}
