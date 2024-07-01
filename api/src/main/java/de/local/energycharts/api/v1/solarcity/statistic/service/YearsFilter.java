package de.local.energycharts.api.v1.solarcity.statistic.service;

import de.local.energycharts.solarcity.model.statistic.AdditionOfSolarInstallations;

import java.util.List;

import static de.local.energycharts.solarcity.model.Time.currentYear;

public class YearsFilter {

  private final boolean previousSolarInstallationsOnly;
  private final int years;
  private final List<AdditionOfSolarInstallations> additions;

  private YearsFilter(
      boolean previousSolarInstallationsOnly,
      int years,
      List<AdditionOfSolarInstallations> additions
  ) {
    this.previousSolarInstallationsOnly = previousSolarInstallationsOnly;
    this.years = years;
    this.additions = additions;
  }

  public static YearsFilter createFilter(
      boolean previousSolarInstallationsOnly,
      int years,
      List<AdditionOfSolarInstallations> additions
  ) {
    return new YearsFilter(previousSolarInstallationsOnly, years, additions);
  }


  public boolean filter(AdditionOfSolarInstallations addition) {
    if (previousSolarInstallationsOnly) {
      return addition.getYear() >= currentYear() - years && addition.getYear() <= currentYear();
    }

    var maxYear = additions.stream().mapToInt(AdditionOfSolarInstallations::getYear).max().orElse(2000);
    return addition.getYear() >= maxYear - years;
  }
}
