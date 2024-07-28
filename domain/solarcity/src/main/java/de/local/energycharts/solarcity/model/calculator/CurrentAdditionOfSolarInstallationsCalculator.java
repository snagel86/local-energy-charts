package de.local.energycharts.solarcity.model.calculator;

import de.local.energycharts.solarcity.model.SolarSystem;
import de.local.energycharts.solarcity.model.statistic.AdditionOfSolarInstallations;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static java.util.stream.Collectors.groupingBy;

@RequiredArgsConstructor
public class CurrentAdditionOfSolarInstallationsCalculator {

  private final Set<SolarSystem> solarSystems;

  public List<AdditionOfSolarInstallations> calculateAnnualAdditions() {
    var additions = new ArrayList<AdditionOfSolarInstallations>();

    solarSystems.stream()
        .collect(groupingBy(SolarSystem::getCommissioningYear))
        .forEach((year, solarSystemsByYear) ->
            additions.add(createAddition(year, solarSystemsByYear))
        );
    return additions;
  }

  private AdditionOfSolarInstallations createAddition(
      Integer year,
      List<SolarSystem> solarSystems
  ) {
    return AdditionOfSolarInstallations.builder()
        .year(year)
        .numberOfSolarSystemsUpTo1kWp(solarSystems.stream()
            .filter(solarSystem -> solarSystem.getInstalledNetPowerkWp() <= 1.0)
            .count()
        )
        .numberOfSolarSystems1To10kWp(solarSystems.stream()
            .filter(solarSystem -> solarSystem.getInstalledNetPowerkWp() > 1.0)
            .filter(solarSystem -> solarSystem.getInstalledNetPowerkWp() <= 10.0)
            .count()
        )
        .numberOfSolarSystems10To40kWp(solarSystems.stream()
            .filter(solarSystem -> solarSystem.getInstalledNetPowerkWp() > 10.0)
            .filter(solarSystem -> solarSystem.getInstalledNetPowerkWp() <= 40.0)
            .count()
        )
        .numberOfSolarSystemsFrom40kWp(solarSystems.stream()
            .filter(solarSystem -> solarSystem.getInstalledNetPowerkWp() > 40.0)
            .count()
        )
        .numberOfSolarSystems(solarSystems.size())
        .totalInstalledMWp(sumTotalInstalledMWp(solarSystems))
        .build();
  }

  private double sumTotalInstalledMWp(List<SolarSystem> solarSystemsByYear) {
    return solarSystemsByYear.stream()
        .mapToDouble(SolarSystem::getInstalledNetPowerkWp)
        .sum() / 1000.0; // kWp -> MWp
  }
}
