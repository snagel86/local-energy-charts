package de.local.energycharts.api.v1.solarcity.statistic;

import de.local.energycharts.api.v1.solarcity.statistic.model.OverviewResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.jmolecules.architecture.hexagonal.PrimaryAdapter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.io.File;

@PrimaryAdapter
@Tag(name = "Solar City")
@RestController
@RequestMapping("/v1/solar-cities")
@RequiredArgsConstructor
public class StatisticApiController {

  private final StatisticApiService statisticApiService;

  @Operation(summary = "Returns a statistical overview of a solar city.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "overview",
          content = {@Content(mediaType = "application/json",
              schema = @Schema(implementation = OverviewResponse.class))})
  })
  @GetMapping(value = "/{id}/statistics/overview", produces = "application/json")
  public Mono<ResponseEntity<OverviewResponse>> getOverview(@PathVariable("id") String id) {
    return statisticApiService.createOverview(id)
        .map(ResponseEntity::ok)
        .defaultIfEmpty(ResponseEntity.notFound().build());
  }

  @Operation(summary = "Creates an Excel sheet that lists all solar installations in a city.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "xls document.",
          content = {@Content(mediaType = "application/json",
              schema = @Schema(implementation = File.class))})
  })
  @GetMapping(value = "/{id}/solar-systems/xls", produces = "application/json")
  public Mono<ResponseEntity<?>> getSolarSystemsXLS(@PathVariable("id") String id) {
    return statisticApiService.getSolarSystemsXls(id);
  }
}
