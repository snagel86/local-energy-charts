package de.local.energycharts.api.v1.statistic.controller;

import de.local.energycharts.api.v1.statistic.model.AdditionOfSolarInstallationsResponse;
import de.local.energycharts.api.v1.statistic.model.SolarCityOverviewResponse;
import de.local.energycharts.api.v1.statistic.model.OperatorOverviewResponse;
import de.local.energycharts.api.v1.statistic.service.SolarCityStatisticApiService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
public class SolarCityStatisticController {

  private final SolarCityStatisticApiService solarCityStatisticApiService;

  @GetMapping(value = "/v1/city/{city}/solar-overview", produces = "application/json")
  public Mono<SolarCityOverviewResponse> getSolarOverview(
      @PathVariable String city
  ) {
    return solarCityStatisticApiService.createSolarCityOverview(city);
  }

  @GetMapping(value = "/v1/city/{city}/annual-addition-of-solar-installations", produces = "application/json")
  public Flux<AdditionOfSolarInstallationsResponse> getAnnualAdditionOfSolarInstallations(
      @PathVariable String city,
      @RequestParam(required = false) boolean previousSolarInstallationsOnly
  ) {
    return solarCityStatisticApiService.createAnnualAdditionOfSolarInstallations(
        city,
        previousSolarInstallationsOnly
    );
  }

  @GetMapping(value = "/v1/city/{city}/operator-overview", produces = "application/json")
  public Flux<OperatorOverviewResponse> getOperatorOverview(@PathVariable String city) {
    return solarCityStatisticApiService.createOperatorOverviews(city);
  }
}
