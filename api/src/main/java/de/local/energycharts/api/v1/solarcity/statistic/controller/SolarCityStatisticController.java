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
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@Tag(name = "Solar City")
@RestController
@RequestMapping("/v1/solar-cities")
@RequiredArgsConstructor
public class SolarCityStatisticController {

  private final SolarCityStatisticApiService solarCityStatisticApiService;
  private final SolarCityStatisticHighchartsApiService solarCityStatisticHighchartsApiService;

  @Operation(summary = "Statistical overview of a solar city.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "overview",
          content = {@Content(mediaType = "application/json",
              schema = @Schema(implementation = SolarCityOverviewResponse.class))})
  })
  @GetMapping(value = "/{solarCityIdOrName}/statistics/overview", produces = "application/json")
  public Mono<SolarCityOverviewResponse> getOverview(@PathVariable String solarCityIdOrName) {
    return solarCityStatisticApiService.createOverview(solarCityIdOrName);
  }

  @Operation(summary = "The column chart shows the annual addition of solar installations in a city. "
      + "The response is optimised for Highcharts."
  )
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "annual addition of solar installations in highcharts format.",
          content = {@Content(mediaType = "application/json",
              schema = @Schema(implementation = AnnualAdditionOfSolarInstallationsChartResponse.class))})
  })
  @GetMapping(value = "/{solarCityIdOrName}/statistics/annual-addition-of-solar-installations/highcharts", produces = "application/json")
  public Mono<AnnualAdditionOfSolarInstallationsChartResponse> getAnnualAdditionOfSolarInstallationsHighcharts(
      @PathVariable String solarCityIdOrName,
      @RequestParam(required = false) boolean previousSolarInstallationsOnly
  ) {
    return solarCityStatisticHighchartsApiService.createAnnualAdditionOfSolarInstallationsChart(
        solarCityIdOrName,
        previousSolarInstallationsOnly
    );
  }

  @Operation(summary = "", hidden = true)
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "monthly solar installations",
          content = {@Content(mediaType = "application/json",
              schema = @Schema(implementation = MonthlySolarInstallationsChartResponse.class))})
  })
  @GetMapping(value = "/{solarCityIdOrName}/statistics/monthly-solar-installations/highcharts", produces = "application/json")
  public Mono<MonthlySolarInstallationsChartResponse> getMonthlySolarInstallationsChart(@PathVariable String solarCityIdOrName) {
    return solarCityStatisticHighchartsApiService.createMonthlySolarInstallationsChart(solarCityIdOrName);
  }

  @Operation(summary = "The pie chart shows the distribution of solar installations on different buildings " +
      "such as homes, apartment buildings, schools or industrial buildings. The response is optimised for Highcharts.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "building pie chart",
          content = {@Content(mediaType = "application/json",
              schema = @Schema(implementation = SolarBuildingPieChartResponse.class))})
  })
  @GetMapping(value = "/{solarCityIdOrName}/statistics/building-pie-chart/highcharts", produces = "application/json")
  public Mono<SolarBuildingPieChartResponse> getSolarBuildingPieChart(@PathVariable String solarCityIdOrName) {
    return solarCityStatisticHighchartsApiService.createSolarBuildingPieChart(solarCityIdOrName);
  }

  @Operation(summary = "", hidden = true)
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "annual addition of solar installations in highcharts format.",
          content = {@Content(mediaType = "application/json",
              schema = @Schema(implementation = AnnualAdditionOfSolarInstallationsChartResponse.class))})
  })
  @PostMapping(value = "/{cityName}/statistics/annual-addition-of-solar-installations/highcharts/temporary", produces = "application/json")
  public Mono<AnnualAdditionOfSolarInstallationsChartResponse> createTemporaryAnnualAdditionOfSolarInstallations(
      @PathVariable String cityName,
      @RequestBody SolarCityRequest request
  ) {
    return solarCityStatisticHighchartsApiService.createTemporaryAnnualAdditionOfSolarInstallationsChart(
        cityName.trim(),
        request.getEntireSolarPotentialOnRooftopsMWp(), request.getTargetYear()
    );
  }
}
