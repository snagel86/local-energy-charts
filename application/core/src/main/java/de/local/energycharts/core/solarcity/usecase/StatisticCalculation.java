package de.local.energycharts.core.solarcity.usecase;

import de.local.energycharts.core.solarcity.model.AnnualSolarInstallations;
import de.local.energycharts.core.solarcity.model.MonthlySolarInstallations;
import de.local.energycharts.core.solarcity.model.Overview;
import de.local.energycharts.core.solarcity.model.SolarBuildingPieChart;
import de.local.energycharts.core.solarcity.model.SolarCity;
import de.local.energycharts.core.solarcity.ports.out.SolarCityCache;
import de.local.energycharts.core.solarcity.ports.in.CalculateStatistic;
import de.local.energycharts.core.solarcity.ports.in.CreateSolarXls;
import de.local.energycharts.core.solarcity.ports.out.SolarSystemsXlsWriter;
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
  private final Administration solarCityAdministration;
  private final SolarSystemsXlsWriter solarSystemsXlsWriter;

  public Mono<AnnualSolarInstallations> annualSolarInstallations(String id) {
    return solarCityCache
        .get(id)
        .map(SolarCity::calculateAnnualSolarInstallations);
  }

  public Mono<AnnualSolarInstallations> annualSolarInstallationsTemporary(
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
