package de.local.energycharts.core.model.statistic;

import de.local.energycharts.core.model.SolarSystem;
import java.time.Instant;
import java.util.Set;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class SolarCityOverviewCalculator {

  private final Double totalSolarPotentialMWp;
  private final Double alreadyInstalledMWp;
  private final Set<SolarSystem> solarSystems;
  private final Instant updated;

  public SolarCityOverview calculateSolarCityOverview() {

    return SolarCityOverview.builder()
        .totalSolarInstallations(solarSystems.size())
        .totalSolarPotentialMWp(totalSolarPotentialMWp)
        .totalInstalledMWp(alreadyInstalledMWp)
        .usedSolarPotentialPercent(
            totalSolarPotentialMWp != null ?
                alreadyInstalledMWp / totalSolarPotentialMWp * 100 : -1.0
        )
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
        .updated(updated)
        .build();
  }
}
