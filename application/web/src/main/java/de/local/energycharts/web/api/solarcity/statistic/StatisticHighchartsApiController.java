package de.local.energycharts.web.api.solarcity.statistic;

import de.local.energycharts.web.api.solarcity.statistic.model.highcharts.AnnualSolarInstallationsChartResponse;
import de.local.energycharts.web.api.solarcity.statistic.model.highcharts.MonthlySolarInstallationsChartResponse;
import de.local.energycharts.web.api.solarcity.statistic.model.highcharts.SolarBuildingPieChartResponse;
import de.local.energycharts.web.api.solarcity.statistic.model.highcharts.SolarCityRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.jmolecules.architecture.hexagonal.PrimaryAdapter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@PrimaryAdapter
@Tag(name = "Solar City")
@RestController
@RequestMapping("/v1/solar-cities")
@RequiredArgsConstructor
public class StatisticHighchartsApiController {

  private final StatisticHighchartsApiService statisticHighchartsApiService;

  @Operation(summary = """
      Returns a column chart showing the annual addition of solar installations in a city.
      The response is optimised for Highcharts.
      """
  )
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "annual addition of solar installations in highcharts format.",
          content = {@Content(mediaType = "application/json",
              schema = @Schema(implementation = AnnualSolarInstallationsChartResponse.class))})
  })
  @GetMapping(value = "/{id}/statistics/annual-addition-of-solar-installations/highcharts", produces = "application/json")
  public Mono<ResponseEntity<AnnualSolarInstallationsChartResponse>> getAnnualAdditionOfSolarInstallationsHighcharts(
      @PathVariable("id") String id,
      @RequestParam(name = "years", required = false, defaultValue = "20") int years,
      @RequestParam(name = "previousSolarInstallationsOnly", required = false) boolean previousSolarInstallationsOnly
  ) {
    return statisticHighchartsApiService.createAnnualSolarInstallationsChart(
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
      """,
      hidden = true
  )
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "annual addition of solar installations in highcharts format.",
          content = {@Content(mediaType = "application/json",
              schema = @Schema(implementation = AnnualSolarInstallationsChartResponse.class))})
  })
  @PostMapping(value = "/{name}/statistics/annual-addition-of-solar-installations/highcharts/temporary", produces = "application/json")
  public Mono<ResponseEntity<AnnualSolarInstallationsChartResponse>> createTemporaryAnnualAdditionOfSolarInstallations(
      @PathVariable("name") String name,
      @RequestBody SolarCityRequest request
  ) {
    return statisticHighchartsApiService.createTemporaryAnnualSolarInstallationsChart(
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
    return statisticHighchartsApiService
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
    return statisticHighchartsApiService
        .createSolarBuildingPieChart(id)
        .map(ResponseEntity::ok)
        .defaultIfEmpty(ResponseEntity.notFound().build());
  }
}
