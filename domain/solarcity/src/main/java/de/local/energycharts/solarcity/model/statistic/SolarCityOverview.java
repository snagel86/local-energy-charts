package de.local.energycharts.solarcity.model.statistic;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;

@Data
@Builder
public class SolarCityOverview {

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
