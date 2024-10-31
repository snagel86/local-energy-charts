package de.local.energycharts.statistic.ports.in;

import de.local.energycharts.statistic.model.AnnualSolarInstallations;
import reactor.core.publisher.Mono;

public interface CalculateStatisticTemporary {

  Mono<AnnualSolarInstallations> annualSolarInstallations(
      String name,
      Double entireSolarPotentialOnRooftopsMWp, Integer targetYear
  );
}
