package de.local.energycharts.core.model.statistic;

import static java.util.stream.Collectors.groupingBy;

import de.local.energycharts.core.model.SolarSystem;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class AdditionOfSolarInstallationsCalculator {

  private final Set<SolarSystem> solarSystems;

  public List<AdditionOfSolarInstallations> calculateAnnualAdditions() {
    List<AdditionOfSolarInstallations> additions = new ArrayList<>();

    solarSystems.stream()
        .collect(groupingBy(SolarSystem::getCommissioningYear))
        .forEach((year, solarSystemsByYear) ->
            additions.add(createAddition(year, null, solarSystemsByYear))
        );
    return additions;
  }

  public List<AdditionOfSolarInstallations> calculateMonthlyAdditions() {
    List<AdditionOfSolarInstallations> additions = new ArrayList<>();

    solarSystems.stream()
        .collect(groupingBy(SolarSystem::getCommissioningYear))
        .forEach((year, solarSystemsByYear) ->
            solarSystemsByYear.stream()
                .collect(groupingBy(SolarSystem::getCommissioningMonth))
                .forEach((month, solarSystemsByYearAndMonth) ->
                    additions.add(createAddition(year, month, solarSystemsByYearAndMonth))
                )
        );
    return additions;
  }

  private AdditionOfSolarInstallations createAddition(
      Integer year, Month month,
      List<SolarSystem> solarSystems
  ) {
    return AdditionOfSolarInstallations.builder()
        .year(year)
        .month(month)
        .numberOfSolarSystemsUpTo10kWp(solarSystems.stream()
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
