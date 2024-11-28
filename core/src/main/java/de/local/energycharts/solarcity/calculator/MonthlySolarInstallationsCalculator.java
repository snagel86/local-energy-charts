package de.local.energycharts.solarcity.calculator;

import de.local.energycharts.solarcity.model.SolarSystem;
import de.local.energycharts.solarcity.model.MonthlySolarInstallations;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static java.util.stream.Collectors.groupingBy;

@RequiredArgsConstructor
public class MonthlySolarInstallationsCalculator {

  private final Set<SolarSystem> solarSystems;

  public MonthlySolarInstallations calculateMonthlyInstallations() {
    var additions = new ArrayList<MonthlySolarInstallations.Addition>();

    solarSystems.stream()
        .filter(SolarSystem::isCommissionedWithinTheLastTwelveMonth)
        .collect(groupingBy(SolarSystem::getCommissioningMonth))
        .forEach((commissioning, solarSystemsByYear) ->
            additions.add(createAddition(commissioning, solarSystemsByYear))
        );
    return MonthlySolarInstallations.builder()
        .additions(additions.stream().sorted().toList()).build();
  }

  private MonthlySolarInstallations.Addition createAddition(
      LocalDate commissioning,
      List<SolarSystem> solarSystems
  ) {
    return MonthlySolarInstallations.Addition.builder()
        .year(commissioning.getYear())
        .month(commissioning.getMonth())
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
