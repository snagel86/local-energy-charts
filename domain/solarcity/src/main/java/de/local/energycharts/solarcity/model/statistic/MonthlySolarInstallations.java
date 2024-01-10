package de.local.energycharts.solarcity.model.statistic;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;

@Data
@Accessors(chain = true)
@Builder
public class MonthlySolarInstallations {

  private List<Addition> additions;

  public Addition getAddition(Integer year, Month month) {
    return additions.stream()
        .filter(addition -> addition.getYear().equals(year) && addition.getMonth().equals(month))
        .findAny()
        .orElse(null);
  }

  @Data
  @Accessors(chain = true)
  @Builder
  public static class Addition implements Comparable<Addition> {
    private Integer year;
    private Month month;
    private Double totalInstalledMWp;
    private Integer numberOfSolarSystems;
    private Long numberOfSolarSystemsUpTo1kWp;
    private Long numberOfSolarSystems1To10kWp;
    private Long numberOfSolarSystems10To40kWp;
    private Long numberOfSolarSystemsFrom40kWp;

    public int compareTo(Addition other) {
      return LocalDate.of(year, month, 1)
          .compareTo(LocalDate.of(other.year, other.month, 1));
    }
  }
}
