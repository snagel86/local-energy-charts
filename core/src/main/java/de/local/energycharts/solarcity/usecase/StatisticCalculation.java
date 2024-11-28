package de.local.energycharts.solarcity.usecase;

import de.local.energycharts.solarcity.SolarCityCache;
import de.local.energycharts.solarcity.model.*;
import de.local.energycharts.solarcity.ports.in.CalculateStatistic;
import de.local.energycharts.solarcity.ports.in.CreateSolarXls;
import de.local.energycharts.solarcity.ports.out.SolarSystemsXlsWriter;
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
  private final SolarSystemsXlsWriter solarSystemsXlsWriter;

  public Mono<AnnualSolarInstallations> annualSolarInstallations(String id) {
    return solarCityCache
        .get(id)
        .map(SolarCity::calculateAnnualSolarInstallations);
  }

  public Mono<MonthlySolarInstallations> monthlySolarInstallations(String id) {
    return solarCityCache
        .get(id)
        .map(SolarCity::calculateMonthlySolarInstallations);
  }

  public Mono<SolarBuildingPieChart> solarBuildingPieChart(String id) {
    return solarCityCache
        .get(id)
        .map(SolarCity::calculateSolarBuildingPieChart);
  }

  public Mono<Overview> solarCityOverview(String id) {
    return solarCityCache
        .get(id)
        .map(SolarCity::calculateSolarCityOverview);
  }

  public Mono<File> allSolarSystems(String id) {
    return solarCityCache.get(id)
        .map(solarSystemsXlsWriter::write);
  }
}
