package de.local.energycharts.api.v1.solarcity.statistic.controller;

import de.local.energycharts.api.v1.solarcity.statistic.model.common.SolarCityOverviewResponse;
import de.local.energycharts.api.v1.solarcity.statistic.model.highcharts.AnnualAdditionOfSolarInstallationsChartResponse;
import de.local.energycharts.api.v1.solarcity.statistic.model.highcharts.MonthlySolarInstallationsChartResponse;
import de.local.energycharts.api.v1.solarcity.statistic.model.highcharts.SolarBuildingPieChartResponse;
import de.local.energycharts.api.v1.solarcity.statistic.model.highcharts.SolarCityRequest;
import de.local.energycharts.api.v1.solarcity.statistic.service.SolarCityStatisticApiService;
import de.local.energycharts.api.v1.solarcity.statistic.service.SolarCityStatisticHighchartsApiService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@Tag(name = "Solar City")
@RestController
@RequestMapping("/v1/solar-cities")
@RequiredArgsConstructor
public class SolarCityStatisticController {

  private final SolarCityStatisticApiService solarCityStatisticApiService;
  private final SolarCityStatisticHighchartsApiService solarCityStatisticHighchartsApiService;

  @Operation(summary = "Returns a statistical overview of a solar city.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "overview",
          content = {@Content(mediaType = "application/json",
              schema = @Schema(implementation = SolarCityOverviewResponse.class))})
  })
  @GetMapping(value = "/{id}/statistics/overview", produces = "application/json")
  public Mono<ResponseEntity<SolarCityOverviewResponse>> getOverview(@PathVariable("id") String id) {
    return solarCityStatisticApiService.createOverview(id)
        .map(ResponseEntity::ok)
        .defaultIfEmpty(ResponseEntity.notFound().build());
  }

  @Operation(summary = """
      Returns a column chart showing the annual addition of solar installations in a city.
      The response is optimised for Highcharts.
      """
  )
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "annual addition of solar installations in highcharts format.",
          content = {@Content(mediaType = "application/json",
              schema = @Schema(implementation = AnnualAdditionOfSolarInstallationsChartResponse.class))})
  })
  @GetMapping(value = "/{id}/statistics/annual-addition-of-solar-installations/highcharts", produces = "application/json")
  public Mono<ResponseEntity<AnnualAdditionOfSolarInstallationsChartResponse>> getAnnualAdditionOfSolarInstallationsHighcharts(
      @PathVariable("id") String id,
      @RequestParam(name = "years", required = false, defaultValue = "20") int years,
      @RequestParam(name = "previousSolarInstallationsOnly", required = false) boolean previousSolarInstallationsOnly
  ) {
    return solarCityStatisticHighchartsApiService.createAnnualAdditionOfSolarInstallationsChart(
            id,
            years,
            previousSolarInstallationsOnly
        ).map(ResponseEntity::ok)
        .defaultIfEmpty(ResponseEntity.notFound().build());
  }

  @Operation(summary = """
      Also shows the annual addition of solar installations,
      but downloads the data on-the-fly from the Marktstammdatenregister
      instead of using the process of a created solar city.
      """)
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "annual addition of solar installations in highcharts format.",
          content = {@Content(mediaType = "application/json",
              schema = @Schema(implementation = AnnualAdditionOfSolarInstallationsChartResponse.class))})
  })
  @PostMapping(value = "/{name}/statistics/annual-addition-of-solar-installations/highcharts/temporary", produces = "application/json")
  public Mono<ResponseEntity<AnnualAdditionOfSolarInstallationsChartResponse>> createTemporaryAnnualAdditionOfSolarInstallations(
      @PathVariable("name") String name,
      @RequestBody SolarCityRequest request
  ) {
    return solarCityStatisticHighchartsApiService.createTemporaryAnnualAdditionOfSolarInstallationsChart(
            name.trim(),
            request
        ).map(ResponseEntity::ok)
        .defaultIfEmpty(ResponseEntity.notFound().build());
  }

  @Operation(summary = "", hidden = true)
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "monthly solar installations",
          content = {@Content(mediaType = "application/json",
              schema = @Schema(implementation = MonthlySolarInstallationsChartResponse.class))})
  })
  @GetMapping(value = "/{id}/statistics/monthly-solar-installations/highcharts", produces = "application/json")
  public Mono<ResponseEntity<MonthlySolarInstallationsChartResponse>> getMonthlySolarInstallationsChart(
      @PathVariable("id") String id
  ) {
    return solarCityStatisticHighchartsApiService
        .createMonthlySolarInstallationsChart(id)
        .map(ResponseEntity::ok)
        .defaultIfEmpty(ResponseEntity.notFound().build());
  }

  @Operation(summary = """
      Returns a pie chart showing the distribution of solar installations on different buildings
      such as homes, apartment buildings, schools or industrial buildings.
      The response is optimised for Highcharts.
      """
  )
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "building pie chart",
          content = {@Content(mediaType = "application/json",
              schema = @Schema(implementation = SolarBuildingPieChartResponse.class))})
  })
  @GetMapping(value = "/{id}/statistics/building-pie-chart/highcharts", produces = "application/json")
  public Mono<ResponseEntity<SolarBuildingPieChartResponse>> getSolarBuildingPieChart(
      @PathVariable("id") String id
  ) {
    return solarCityStatisticHighchartsApiService
        .createSolarBuildingPieChart(id)
        .map(ResponseEntity::ok)
        .defaultIfEmpty(ResponseEntity.notFound().build());
  }
}
