package de.local.energycharts.statistic.usecase;

import de.local.energycharts.solarcity.usecase.Administration;
import de.local.energycharts.statistic.model.AnnualSolarInstallations;
import de.local.energycharts.statistic.model.SolarCityStatistic;
import de.local.energycharts.statistic.model.mapper.SolarCityStatisticMapper;
import de.local.energycharts.statistic.ports.in.CalculateStatisticTemporary;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class StatisticTemporaryCalculation implements CalculateStatisticTemporary {

  private final Administration solarCityAdministration;
  private final SolarCityStatisticMapper solarCityStatisticMapper;

  public Mono<AnnualSolarInstallations> annualSolarInstallations(
      String name,
      Double entireSolarPotentialOnRooftopsMWp, Integer targetYear
  ) {
    return solarCityAdministration
        .createTemporary(
            name,
            entireSolarPotentialOnRooftopsMWp, targetYear
        )
        .map(solarCityStatisticMapper::map)
        .map(SolarCityStatistic::calculateAnnualSolarInstallations);
  }
}
