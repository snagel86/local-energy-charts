package de.local.energycharts.solarcity.calculator;

import de.local.energycharts.solarcity.model.AnnualSolarInstallations;
import de.local.energycharts.solarcity.model.SolarCity;

import java.util.List;

import static de.local.energycharts.solarcity.model.AnnualSolarInstallations.Addition;

public class AnnualSolarInstallationsCalculator {

  private final SolarCity solarCity;
  private final OverviewCalculator overviewCalculator;

  public AnnualSolarInstallationsCalculator(SolarCity solarCity) {
    this.solarCity = solarCity;
    this.overviewCalculator = new OverviewCalculator(solarCity);
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
