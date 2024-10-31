package de.local.energycharts.api.v1.statistic;

import de.local.energycharts.statistic.model.AnnualSolarInstallations;

import java.util.List;

import static de.local.energycharts.solarcity.model.Time.currentYear;

public class YearsFilter {

  private final boolean previousSolarInstallationsOnly;
  private final int years;
  private final List<AnnualSolarInstallations.Addition> additions;

  private YearsFilter(
      boolean previousSolarInstallationsOnly,
      int years,
      List<AnnualSolarInstallations.Addition> additions
  ) {
    this.previousSolarInstallationsOnly = previousSolarInstallationsOnly;
    this.years = years;
    this.additions = additions;
  }

  public static YearsFilter createFilter(
      boolean previousSolarInstallationsOnly,
      int years,
      List<AnnualSolarInstallations.Addition> additions
  ) {
    return new YearsFilter(previousSolarInstallationsOnly, years, additions);
  }


  public boolean filter(AnnualSolarInstallations.Addition addition) {
    if (previousSolarInstallationsOnly) {
      return addition.getYear() >= currentYear() - years && addition.getYear() <= currentYear();
    }

    var maxYear = additions.stream().mapToInt(AnnualSolarInstallations.Addition::getYear).max().orElse(2000);
    return addition.getYear() >= maxYear - years;
  }
}
