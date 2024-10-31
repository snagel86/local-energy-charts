package de.local.energycharts.api.v1.statistic.model.highcharts;

import lombok.Data;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Data
public class PieSlice {

  private String id;
  private String name;
  private Double y; // percentage
  private Double installedMWp;
  private Integer count;

  public Double getY() {
    return BigDecimal.valueOf(y).setScale(2, RoundingMode.HALF_UP).doubleValue();
  }

  public Double getInstalledMWp() {
    return BigDecimal.valueOf(installedMWp).setScale(2, RoundingMode.HALF_UP).doubleValue();
  }
}
