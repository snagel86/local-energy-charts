package de.local.energycharts.api.v1.solarcity.statistic.service;

import de.local.energycharts.api.v1.solarcity.statistic.model.highcharts.AnnualAdditionOfSolarInstallationsChartResponse;
import de.local.energycharts.api.v1.solarcity.statistic.model.highcharts.MonthlySolarInstallationsChartResponse;
import de.local.energycharts.api.v1.solarcity.statistic.model.highcharts.SolarBuildingPieChartResponse;
import de.local.energycharts.api.v1.solarcity.statistic.model.highcharts.SolarCityRequest;
import de.local.energycharts.api.v1.solarcity.statistic.model.highcharts.mapper.ColumnMapper;
import de.local.energycharts.api.v1.solarcity.statistic.model.highcharts.mapper.MonthlySolarInstallationsChartMapper;
import de.local.energycharts.api.v1.solarcity.statistic.model.highcharts.mapper.SolarBuildingPieChartMapper;
import de.local.energycharts.solarcity.model.SolarCity;
import de.local.energycharts.solarcity.model.statistic.AnnualSolarInstallations;
import de.local.energycharts.solarcity.service.SolarCityService;
import de.local.energycharts.solarcity.service.SolarCityStatisticService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import static de.local.energycharts.api.v1.solarcity.statistic.service.YearsFilter.createFilter;

@Service
@RequiredArgsConstructor
public class SolarCityStatisticHighchartsApiService {

  private final SolarCityService solarCityService;
  private final SolarCityStatisticService solarCityStatisticService;
  private final ColumnMapper columnMapper;
  private final SolarBuildingPieChartMapper solarBuildingPieChartMapper;
  private final MonthlySolarInstallationsChartMapper monthlySolarInstallationsChartMapper;

  public Mono<AnnualAdditionOfSolarInstallationsChartResponse> createAnnualAdditionOfSolarInstallationsChart(
      String id,
      int years,
      boolean previousSolarInstallationsOnly
  ) {
    return solarCityStatisticService
        .calculateAnnualSolarInstallations(id)
        .map(solarCity -> createColumnChartResponse(
            solarCity, previousSolarInstallationsOnly,
            years
        ));
  }

  public Mono<AnnualAdditionOfSolarInstallationsChartResponse> createTemporaryAnnualAdditionOfSolarInstallationsChart(
      String name,
      SolarCityRequest request
  ) {
    return solarCityService
        .createSolarCityTemporary(
            name,
            request.getSolarRoofPotentialMWp(), request.getTargetYear()
        )
        .map(solarCity -> createColumnChartResponse(
            solarCity.calculateAnnualSolarInstallations(),
            false,
            request.getYears())
        ).map(this::throwErrorWhenEmpty);
  }

  public Mono<SolarBuildingPieChartResponse> createSolarBuildingPieChart(String id) {
    return solarCityStatisticService.calculateSolarBuildingPieChart(id)
        .map(solarBuildingPieChartMapper::mapToResponse);
  }

  public Mono<MonthlySolarInstallationsChartResponse> createMonthlySolarInstallationsChart(String id) {
    return solarCityStatisticService.calculateMonthlySolarInstallations(id)
        .map(monthlySolarInstallationsChartMapper::mapToResponse);
  }

  private AnnualAdditionOfSolarInstallationsChartResponse createColumnChartResponse(
      AnnualSolarInstallations solarInstallations,
      boolean previousSolarInstallationsOnly,
      int years
  ) {
    var yearsFilter = createFilter(previousSolarInstallationsOnly, years, solarInstallations.getAdditions());

    // data
    return AnnualAdditionOfSolarInstallationsChartResponse.builder()
        .cityName(solarInstallations.getCityName())
        .columns(solarInstallations.getAdditions().stream()
            .filter(yearsFilter::filter)
            .map(columnMapper::mapToColumn).toList())
        .rooftopSolarSystemsInOperation(solarInstallations.getRooftopSolarSystemsInOperation())
        .installedRooftopMWpInOperation(solarInstallations.getInstalledRooftopMWpInOperation())
        .build();
  }

  private AnnualAdditionOfSolarInstallationsChartResponse throwErrorWhenEmpty(AnnualAdditionOfSolarInstallationsChartResponse response) {
    if (response.isEmpty()) {
      throw new IllegalStateException("city does not exist!");
    } else {
      return response;
    }
  }
}
