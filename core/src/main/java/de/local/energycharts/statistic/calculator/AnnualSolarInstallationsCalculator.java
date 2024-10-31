package de.local.energycharts.statistic.calculator;

import de.local.energycharts.statistic.model.SolarCityStatistic;
import de.local.energycharts.statistic.model.AnnualSolarInstallations;

import java.util.List;

import static de.local.energycharts.statistic.model.AnnualSolarInstallations.Addition;

public class AnnualSolarInstallationsCalculator {

  private final SolarCityStatistic solarCity;
  private final SolarCityOverviewCalculator overviewCalculator;

  public AnnualSolarInstallationsCalculator(SolarCityStatistic solarCity) {
    this.solarCity = solarCity;
    this.overviewCalculator = new SolarCityOverviewCalculator(solarCity);
  }

  public AnnualSolarInstallations calculateAnnualSolarInstallations() {
    var additions = calculateCurrentAdditions();

    if (isEntireSolarPotentialAndTargetYearSpecified()) {
      var futureAdditions = calculateFutureAdditions(additions);
      additions.addAll(futureAdditions);
    }

    return AnnualSolarInstallations.builder()
        .cityName(solarCity.getName())
        .additions(additions.stream().sorted().toList())
        .installedRooftopMWpInOperation(overviewCalculator.calculateInstalledRooftopMWpInOperation())
        .rooftopSolarSystemsInOperation(overviewCalculator.calculateRooftopSolarSystemsInOperation())
        .build();
  }

  private List<Addition> calculateCurrentAdditions() {
    return new AnnualCurrentSolarInstallationsCalculator(
        solarCity.getSolarSystems()
    ).calculateAnnualAdditions();
  }

  private boolean isEntireSolarPotentialAndTargetYearSpecified() {
    return solarCity.getEntireSolarPotentialOnRooftopsMWp() != null && solarCity.getTargetYear() != null;
  }

  private List<Addition> calculateFutureAdditions(
      List<Addition> additionsDoneYet
  ) {
    return new AnnualFutureSolarInstallationsCalculator(
        solarCity,
        additionsDoneYet
    ).calculateAnnualAdditions();
  }
}
