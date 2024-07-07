package de.local.energycharts.solarcity.service;

import de.local.energycharts.solarcity.model.SolarCity;
import de.local.energycharts.solarcity.model.statistic.MonthlySolarInstallations;
import de.local.energycharts.solarcity.model.statistic.SolarBuildingPieChart;
import de.local.energycharts.solarcity.model.statistic.SolarCityOverview;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class SolarCityStatisticService {

  private final SolarCityService solarCityService;

  public Mono<SolarCityOverview> createOverview(String city) {
    return solarCityService.getCachedSolarCity(city).map(SolarCity::calculateSolarCityOverview);
  }

  public Mono<SolarBuildingPieChart> createSolarBuildingPieChart(String city) {
    return solarCityService.getCachedSolarCity(city).map(SolarCity::calculateSolarBuildingPieChart);
  }

  public Mono<MonthlySolarInstallations> createMonthlySolarInstallations(String city) {
    return solarCityService.getCachedSolarCity(city).map(SolarCity::calculateMonthlySolarInstallations);
  }
}
