package de.local.energycharts.core.model.statistic;

import static java.util.stream.Collectors.groupingBy;

import de.local.energycharts.core.model.District;
import de.local.energycharts.core.model.SolarSystem;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class OperatorOverviewCalculator {

  private final Set<District> districts;

  public Set<OperatorOverview> calculateOperatorOverview() {
    Set<OperatorOverview> operatorOverviews = new HashSet<>();

    districts.stream()
        .map(District::getSolarSystems)
        .flatMap(Collection::stream).collect(Collectors.toSet()).stream()
        .filter(SolarSystem::isInOperation)
        .collect(groupingBy(SolarSystem::getOperatorName))
        .forEach((operatorName, solarSystemsByOperator) ->
            operatorOverviews.add(OperatorOverview.builder()
                .operatorName(operatorName)
                .numberOfSolarSystems((long) solarSystemsByOperator.size())
                .numberOfSolarSystemsUpTo10kWp(solarSystemsByOperator.stream()
                    .filter(solarSystem -> solarSystem.getInstalledNetPowerkWp() <= 10.0)
                    .count()
                )
                .numberOfSolarSystems10To40kWp(solarSystemsByOperator.stream()
                    .filter(solarSystem -> solarSystem.getInstalledNetPowerkWp() > 10.0)
                    .filter(solarSystem -> solarSystem.getInstalledNetPowerkWp() <= 40.0)
                    .count()
                )
                .numberOfSolarSystemsFrom40kWp(solarSystemsByOperator.stream()
                    .filter(solarSystem -> solarSystem.getInstalledNetPowerkWp() > 40.0)
                    .count()
                )
                .build()
            )
        );
    return operatorOverviews;
  }
}
