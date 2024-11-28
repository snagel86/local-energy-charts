package de.local.energycharts.solarcity.ports.in;

import de.local.energycharts.solarcity.model.AnnualSolarInstallations;
import org.jmolecules.architecture.hexagonal.PrimaryPort;
import reactor.core.publisher.Mono;

@PrimaryPort
public interface CalculateStatisticTemporary {

  Mono<AnnualSolarInstallations> annualSolarInstallations(
      String name,
      Double entireSolarPotentialOnRooftopsMWp, Integer targetYear
  );
}
