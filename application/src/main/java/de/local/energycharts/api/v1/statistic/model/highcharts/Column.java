package de.local.energycharts.api.v1.statistic.model.highcharts;

import java.math.BigDecimal;
import java.math.RoundingMode;
import lombok.Data;

@Data
public class Column {

  private String id;
  private String name;
  private Integer year;
  private Double y; // totalInstalledMWp
  private Integer numberOfSolarSystems;
  private Long numberOfSolarSystemsUpTo1kWp;
  private Long numberOfSolarSystems1To10kWp;
  private Long numberOfSolarSystems10To40kWp;
  private Long numberOfSolarSystemsFrom40kWp;

  public Double getY() {
    return BigDecimal.valueOf(y).setScale(2, RoundingMode.HALF_UP).doubleValue();
  }
}
