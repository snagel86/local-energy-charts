package de.local.energycharts.statistic.model;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.List;

import static de.local.energycharts.solarcity.model.Time.currentYear;
import static java.math.RoundingMode.HALF_DOWN;

@Data
@Accessors(chain = true)
@Builder
public class AnnualSolarInstallations {

  private String cityName;
  private List<Addition> additions;
  private Long rooftopSolarSystemsInOperation;
  private Double installedRooftopMWpInOperation;

  @Data
  @Accessors(chain = true)
  @Builder
  public static class Addition implements Comparable<Addition> {
    private Integer year;
    private Integer numberOfSolarSystems;
    private Long numberOfSolarSystemsUpTo1kWp;
    private Long numberOfSolarSystems1To10kWp;
    private Long numberOfSolarSystems10To40kWp;
    private Long numberOfSolarSystemsFrom40kWp;
    private Double totalInstalledMWp;

    public boolean isCurrent() {
      return year.equals(currentYear());
    }

    public boolean isFuture() {
      return year > currentYear();
    }

    public Double getTotalInstalledMWp() {
      return BigDecimal.valueOf(totalInstalledMWp)
          .setScale(2, HALF_DOWN)
          .doubleValue();
    }

    public int compareTo(Addition other) {
      return year.compareTo(other.year);
    }
  }
}
