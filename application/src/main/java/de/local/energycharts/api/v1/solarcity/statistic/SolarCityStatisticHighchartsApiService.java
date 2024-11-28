package de.local.energycharts.api.v1.solarcity.statistic;

import de.local.energycharts.api.v1.solarcity.statistic.model.highcharts.AnnualSolarInstallationsChartResponse;
import de.local.energycharts.api.v1.solarcity.statistic.model.highcharts.MonthlySolarInstallationsChartResponse;
import de.local.energycharts.api.v1.solarcity.statistic.model.highcharts.SolarBuildingPieChartResponse;
import de.local.energycharts.api.v1.solarcity.statistic.model.highcharts.SolarCityRequest;
import de.local.energycharts.api.v1.solarcity.statistic.model.highcharts.mapper.ColumnMapper;
import de.local.energycharts.api.v1.solarcity.statistic.model.highcharts.mapper.MonthlySolarInstallationsChartMapper;
import de.local.energycharts.api.v1.solarcity.statistic.model.highcharts.mapper.SolarBuildingPieChartMapper;
import de.local.energycharts.solarcity.model.AnnualSolarInstallations;
import de.local.energycharts.solarcity.ports.in.CalculateStatistic;
import de.local.energycharts.solarcity.ports.in.CalculateStatisticTemporary;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import static de.local.energycharts.api.v1.solarcity.statistic.YearsFilter.createFilter;

@Service
@RequiredArgsConstructor
public class SolarCityStatisticHighchartsApiService {

  private final CalculateStatistic calculateStatistic;
  private final CalculateStatisticTemporary calculateStatisticTemporary;

  private final ColumnMapper columnMapper;
  private final SolarBuildingPieChartMapper solarBuildingPieChartMapper;
  private final MonthlySolarInstallationsChartMapper monthlySolarInstallationsChartMapper;

  public Mono<AnnualSolarInstallationsChartResponse> createAnnualSolarInstallationsChart(
      String id,
      int years,
      boolean previousSolarInstallationsOnly
  ) {
    return calculateStatistic
        .annualSolarInstallations(id)
        .map(solarCity -> createColumnChartResponse(
            solarCity, previousSolarInstallationsOnly,
            years
        ));
  }

  public Mono<AnnualSolarInstallationsChartResponse> createTemporaryAnnualSolarInstallationsChart(
      String name,
      SolarCityRequest request
  ) {
    return calculateStatisticTemporary.annualSolarInstallations(
            name,
            request.getSolarRoofPotentialMWp(), request.getTargetYear()
        ).map(annualStatistic -> createColumnChartResponse(
            annualStatistic,
            false,
            request.getYears())
        )
        .map(this::throwErrorWhenEmpty);
  }

  public Mono<SolarBuildingPieChartResponse> createSolarBuildingPieChart(String id) {
    return calculateStatistic.solarBuildingPieChart(id)
        .map(solarBuildingPieChartMapper::mapToResponse);
  }

  public Mono<MonthlySolarInstallationsChartResponse> createMonthlySolarInstallationsChart(String id) {
    return calculateStatistic.monthlySolarInstallations(id)
        .map(monthlySolarInstallationsChartMapper::mapToResponse);
  }

  private AnnualSolarInstallationsChartResponse createColumnChartResponse(
      AnnualSolarInstallations solarInstallations,
      boolean previousSolarInstallationsOnly,
      int years
  ) {
    var yearsFilter = createFilter(previousSolarInstallationsOnly, years, solarInstallations.getAdditions());

    // data
    return AnnualSolarInstallationsChartResponse.builder()
        .cityName(solarInstallations.getCityName())
        .columns(solarInstallations.getAdditions().stream()
            .filter(yearsFilter::filter)
            .map(columnMapper::mapToColumn).toList())
        .rooftopSolarSystemsInOperation(solarInstallations.getRooftopSolarSystemsInOperation())
        .installedRooftopMWpInOperation(solarInstallations.getInstalledRooftopMWpInOperation())
        .build();
  }

  private AnnualSolarInstallationsChartResponse throwErrorWhenEmpty(AnnualSolarInstallationsChartResponse response) {
    if (response.isEmpty()) {
      throw new IllegalStateException("city does not exist!");
    } else {
      return response;
    }
  }
}
