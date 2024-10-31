package de.local.energycharts.statistic.ports.in;

import de.local.energycharts.statistic.model.AnnualSolarInstallations;
import de.local.energycharts.statistic.model.MonthlySolarInstallations;
import de.local.energycharts.statistic.model.SolarBuildingPieChart;
import de.local.energycharts.statistic.model.SolarCityOverview;
import reactor.core.publisher.Mono;

public interface CalculateStatistic {

  Mono<AnnualSolarInstallations> annualSolarInstallations(String id);

  Mono<MonthlySolarInstallations> monthlySolarInstallations(String id);

  Mono<SolarBuildingPieChart> solarBuildingPieChart(String id);

  Mono<SolarCityOverview> solarCityOverview(String id);
}
