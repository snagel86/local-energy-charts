package de.local.energycharts.solarcity.service;

import de.local.energycharts.solarcity.model.SolarCity;
import de.local.energycharts.solarcity.model.statistic.AnnualSolarInstallations;
import de.local.energycharts.solarcity.model.statistic.MonthlySolarInstallations;
import de.local.energycharts.solarcity.model.statistic.SolarBuildingPieChart;
import de.local.energycharts.solarcity.model.statistic.SolarCityOverview;
import de.local.energycharts.solarcity.usecase.CalculateAnnualSolarInstallations;
import de.local.energycharts.solarcity.usecase.CalculateMonthlySolarInstallations;
import de.local.energycharts.solarcity.usecase.CalculateSolarBuildingPieChart;
import de.local.energycharts.solarcity.usecase.CalculateSolarCityOverview;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class SolarCityStatisticService implements
    CalculateAnnualSolarInstallations,
    CalculateMonthlySolarInstallations,
    CalculateSolarBuildingPieChart,
    CalculateSolarCityOverview {

  private final SolarCityCacheService solarCityCacheService;

  public Mono<AnnualSolarInstallations> calculateAnnualSolarInstallations(String id) {
    return solarCityCacheService
        .get(id)
        .map(SolarCity::calculateAnnualSolarInstallations);
  }

  public Mono<MonthlySolarInstallations> calculateMonthlySolarInstallations(String id) {
    return solarCityCacheService
        .get(id)
        .map(SolarCity::calculateMonthlySolarInstallations);
  }

  public Mono<SolarBuildingPieChart> calculateSolarBuildingPieChart(String id) {
    return solarCityCacheService
        .get(id)
        .map(SolarCity::calculateSolarBuildingPieChart);
  }

  public Mono<SolarCityOverview> calculateSolarCityOverview(String id) {
    return solarCityCacheService
        .get(id)
        .map(SolarCity::calculateSolarCityOverview);
  }
}
