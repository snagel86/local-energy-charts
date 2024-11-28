package de.local.energycharts.solarcity.usecase;

import de.local.energycharts.solarcity.model.AnnualSolarInstallations;
import de.local.energycharts.solarcity.model.SolarCity;
import de.local.energycharts.solarcity.ports.in.CalculateStatisticTemporary;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class StatisticTemporaryCalculation implements CalculateStatisticTemporary {

  private final Administration solarCityAdministration;

  public Mono<AnnualSolarInstallations> annualSolarInstallations(
      String name,
      Double entireSolarPotentialOnRooftopsMWp, Integer targetYear
  ) {
    return solarCityAdministration
        .createTemporary(
            name,
            entireSolarPotentialOnRooftopsMWp, targetYear
        )
        .map(SolarCity::calculateAnnualSolarInstallations);
  }
}
