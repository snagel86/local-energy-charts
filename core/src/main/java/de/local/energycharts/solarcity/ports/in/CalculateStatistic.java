package de.local.energycharts.solarcity.ports.in;

import de.local.energycharts.solarcity.model.AnnualSolarInstallations;
import de.local.energycharts.solarcity.model.MonthlySolarInstallations;
import de.local.energycharts.solarcity.model.SolarBuildingPieChart;
import de.local.energycharts.solarcity.model.Overview;
import org.jmolecules.architecture.hexagonal.PrimaryPort;
import reactor.core.publisher.Mono;

@PrimaryPort
public interface CalculateStatistic {

  Mono<AnnualSolarInstallations> annualSolarInstallations(String id);

  Mono<MonthlySolarInstallations> monthlySolarInstallations(String id);

  Mono<SolarBuildingPieChart> solarBuildingPieChart(String id);

  Mono<Overview> solarCityOverview(String id);
}
