package de.local.energycharts.api.v1.solarcity.statistic.service;

import lombok.RequiredArgsConstructor;

import static de.local.energycharts.solarcity.model.Time.currentYear;

@RequiredArgsConstructor
public class RangeOfPreviouslySolarInstallationsCalculator {

  private final boolean previousSolarInstallationsOnly;
  private final Integer targetYear;

  public static RangeOfPreviouslySolarInstallationsCalculator calculateRange(
      boolean previousSolarInstallationsOnly,
      Integer targetYear
  ) {
    return new RangeOfPreviouslySolarInstallationsCalculator(previousSolarInstallationsOnly, targetYear);
  }

  public Integer inYears() {
    if (!previousSolarInstallationsOnly) {
      if (targetYear != null) {
        return 10;
      }
      return 20;
    }

    if (targetYear != null) {
      return targetYear - currentYear() + 10;
    }
    return 20;
  }
}
