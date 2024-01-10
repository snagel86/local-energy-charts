package de.local.energycharts.api.v1.solarcity.statistic.model.common;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import lombok.Data;

@Data
public class SolarCityOverviewResponse {

  private Long totalRoofSolarInstallations;
  private Double totalInstalledRoofMWp;
  private Double entireSolarPotentialOnRooftopsMWp;
  private Integer targetYear;
  private Double averageRoofSolarSystemMWp;
  private Integer roofSolarSystemsToBeInstalledByTheTargetYear;
  private Double usedRoofSolarPotentialPercent;
  private Long totalBalkonSolarInstallations;
  private Double totalInstalledBalkonMWp;
  private Instant updated;

  public Double getTotalInstalledRoofMWp() {
    return BigDecimal.valueOf(totalInstalledRoofMWp)
        .setScale(2, RoundingMode.HALF_DOWN).doubleValue();
  }

  public Double getUsedRoofSolarPotentialPercent() {
    return BigDecimal.valueOf(usedRoofSolarPotentialPercent)
        .setScale(2, RoundingMode.HALF_DOWN).doubleValue();
  }

  public Double getAverageRoofSolarSystemMWp() {
    return BigDecimal.valueOf(averageRoofSolarSystemMWp)
        .setScale(2, RoundingMode.HALF_DOWN).doubleValue();
  }

  public Double getTotalInstalledBalkonMWp() {
    return BigDecimal.valueOf(totalInstalledBalkonMWp)
        .setScale(2, RoundingMode.HALF_DOWN).doubleValue();
  }
}
