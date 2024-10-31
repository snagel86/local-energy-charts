package de.local.energycharts.statistic.usecase;

import de.local.energycharts.statistic.model.*;
import de.local.energycharts.statistic.model.SolarCityStatistic;
import de.local.energycharts.solarcity.usecase.SolarCityCache;
import de.local.energycharts.statistic.model.mapper.SolarCityStatisticMapper;
import de.local.energycharts.statistic.ports.in.CalculateStatistic;
import de.local.energycharts.statistic.ports.in.CreateSolarXls;
import de.local.energycharts.statistic.ports.out.SolarSystemsXlsWriter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.io.File;

@Service
@RequiredArgsConstructor
public class StatisticCalculation implements
    CalculateStatistic,
    CreateSolarXls {

  private final SolarCityCache solarCityCache;
  private final SolarCityStatisticMapper solarCityStatisticMapper;
  private final SolarSystemsXlsWriter solarSystemsXlsWriter;

  public Mono<AnnualSolarInstallations> annualSolarInstallations(String id) {
    return solarCityCache
        .get(id)
        .map(solarCityStatisticMapper::map)
        .map(SolarCityStatistic::calculateAnnualSolarInstallations);
  }

  public Mono<MonthlySolarInstallations> monthlySolarInstallations(String id) {
    return solarCityCache
        .get(id)
        .map(solarCityStatisticMapper::map)
        .map(SolarCityStatistic::calculateMonthlySolarInstallations);
  }

  public Mono<SolarBuildingPieChart> solarBuildingPieChart(String id) {
    return solarCityCache
        .get(id)
        .map(solarCityStatisticMapper::map)
        .map(SolarCityStatistic::calculateSolarBuildingPieChart);
  }

  public Mono<SolarCityOverview> solarCityOverview(String id) {
    return solarCityCache
        .get(id)
        .map(solarCityStatisticMapper::map)
        .map(SolarCityStatistic::calculateSolarCityOverview);
  }

  public Mono<File> allSolarSystems(String id) {
    return solarCityCache.get(id)
        .map(solarSystemsXlsWriter::write);
  }
}
