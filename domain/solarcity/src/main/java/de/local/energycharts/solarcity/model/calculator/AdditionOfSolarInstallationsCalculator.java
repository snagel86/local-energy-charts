package de.local.energycharts.solarcity.model.calculator;

import de.local.energycharts.solarcity.model.SolarCity;
import de.local.energycharts.solarcity.model.statistic.AdditionOfSolarInstallations;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class AdditionOfSolarInstallationsCalculator {

  private final SolarCity solarCity;

  public List<AdditionOfSolarInstallations> calculateAnnualAdditionOfSolarInstallations() {
    var additions = calculateCurrentAnnualAdditionOfSolarSystems();

    if (isEntireSolarPotentialAndTargetYearSpecified()) {
      var futureAdditions = calculateFutureAnnualAdditionOfSolarSystems(additions);
      additions.addAll(futureAdditions);
    }

    return additions.stream()
        .map(addition -> addition.setCityName(solarCity.getName()))
        .sorted()
        .toList();
  }

  private List<AdditionOfSolarInstallations> calculateCurrentAnnualAdditionOfSolarSystems() {
    return new CurrentAdditionOfSolarInstallationsCalculator(
        solarCity.getSolarSystems()
    ).calculateAnnualAdditions();
  }

  private boolean isEntireSolarPotentialAndTargetYearSpecified() {
    return solarCity.getEntireSolarPotentialOnRooftopsMWp() != null && solarCity.getTargetYear() != null;
  }

  private List<AdditionOfSolarInstallations> calculateFutureAnnualAdditionOfSolarSystems(
      List<AdditionOfSolarInstallations> additionsDoneYet
  ) {
    return new FutureAdditionOfSolarInstallationsCalculator(
        solarCity,
        additionsDoneYet
    ).calculateAnnualAdditions();
  }
}
