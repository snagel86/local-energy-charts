package de.local.energycharts.core.solarcity.ports.in;

import de.local.energycharts.core.solarcity.model.AnnualSolarInstallations;
import de.local.energycharts.core.solarcity.model.MonthlySolarInstallations;
import de.local.energycharts.core.solarcity.model.Overview;
import de.local.energycharts.core.solarcity.model.SolarBuildingPieChart;
import org.jmolecules.architecture.hexagonal.PrimaryPort;
import reactor.core.publisher.Mono;

@PrimaryPort
public interface CalculateStatistic {

  Mono<AnnualSolarInstallations> annualSolarInstallations(String id);

  Mono<AnnualSolarInstallations> annualSolarInstallationsTemporary(
      String name,
      Double entireSolarPotentialOnRooftopsMWp, Integer targetYear
  );

  Mono<MonthlySolarInstallations> monthlySolarInstallations(String id);

  Mono<SolarBuildingPieChart> solarBuildingPieChart(String id);

  Mono<Overview> solarCityOverview(String id);
}
