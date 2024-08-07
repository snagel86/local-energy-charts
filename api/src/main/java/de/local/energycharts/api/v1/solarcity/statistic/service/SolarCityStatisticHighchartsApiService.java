package de.local.energycharts.api.v1.solarcity.statistic.service;

import de.local.energycharts.api.v1.solarcity.statistic.model.highcharts.AnnualAdditionOfSolarInstallationsChartResponse;
import de.local.energycharts.api.v1.solarcity.statistic.model.highcharts.MonthlySolarInstallationsChartResponse;
import de.local.energycharts.api.v1.solarcity.statistic.model.highcharts.SolarBuildingPieChartResponse;
import de.local.energycharts.api.v1.solarcity.statistic.model.highcharts.SolarCityRequest;
import de.local.energycharts.api.v1.solarcity.statistic.model.highcharts.mapper.ColumnMapper;
import de.local.energycharts.api.v1.solarcity.statistic.model.highcharts.mapper.MonthlySolarInstallationsChartMapper;
import de.local.energycharts.api.v1.solarcity.statistic.model.highcharts.mapper.SolarBuildingPieChartMapper;
import de.local.energycharts.solarcity.model.SolarCity;
import de.local.energycharts.solarcity.service.SolarCityService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import static de.local.energycharts.api.v1.solarcity.statistic.service.YearsFilter.createFilter;

@Service
@RequiredArgsConstructor
public class SolarCityStatisticHighchartsApiService {

  private final SolarCityService solarCityService;
  private final ColumnMapper columnMapper;
  private final SolarBuildingPieChartMapper solarBuildingPieChartMapper;
  private final MonthlySolarInstallationsChartMapper monthlySolarInstallationsChartMapper;

  public Mono<AnnualAdditionOfSolarInstallationsChartResponse> createAnnualAdditionOfSolarInstallationsChart(
      String id,
      int years,
      boolean previousSolarInstallationsOnly
  ) {
    return solarCityService.getCachedSolarCity(id)
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
        .map(solarCity -> createColumnChartResponse(solarCity, false, request.getYears()))
        .map(this::throwErrorWhenEmpty);
  }

  private AnnualAdditionOfSolarInstallationsChartResponse createColumnChartResponse(
      SolarCity solarCity,
      boolean previousSolarInstallationsOnly,
      int years
  ) {
    var additionOfSolarInstallations = solarCity.calculateAnnualAdditionOfSolarInstallations();
    var yearsFilter = createFilter(previousSolarInstallationsOnly, years, additionOfSolarInstallations);

    // data
    var response = AnnualAdditionOfSolarInstallationsChartResponse.builder()
        .cityName(solarCity.getName())
        .columns(additionOfSolarInstallations.stream()
            .filter(yearsFilter::filter)
            .map(columnMapper::mapToColumn).toList())
        .build();

    // overview
    var solarOverview = solarCity.calculateSolarCityOverview();
    response
        .setRooftopSolarSystemsInOperation(solarOverview.getRooftopSolarSystemsInOperation())
        .setInstalledRooftopMWpInOperation(solarOverview.getInstalledRooftopMWpInOperation());

    return response;
  }

  private AnnualAdditionOfSolarInstallationsChartResponse throwErrorWhenEmpty(AnnualAdditionOfSolarInstallationsChartResponse response) {
    if (response.isEmpty()) {
      throw new IllegalStateException("city does not exist!");
    } else {
      return response;
    }
  }

  public Mono<SolarBuildingPieChartResponse> createSolarBuildingPieChart(String id) {
    return solarCityService.getCachedSolarCity(id)
        .map(SolarCity::calculateSolarBuildingPieChart)
        .map(solarBuildingPieChartMapper::mapToResponse);
  }

  public Mono<MonthlySolarInstallationsChartResponse> createMonthlySolarInstallationsChart(String id) {
    return solarCityService.getCachedSolarCity(id)
        .map(SolarCity::calculateMonthlySolarInstallations)
        .map(monthlySolarInstallationsChartMapper::mapToResponse);
  }
}
