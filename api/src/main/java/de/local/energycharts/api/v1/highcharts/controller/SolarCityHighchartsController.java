package de.local.energycharts.api.v1.highcharts.controller;

import de.local.energycharts.api.v1.highcharts.model.SolarCityHighchartsResponse;
import de.local.energycharts.api.v1.highcharts.model.SolarCityRequest;
import de.local.energycharts.api.v1.highcharts.service.SolarCityHighchartsApiService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
public class SolarCityHighchartsController {

  private final SolarCityHighchartsApiService solarCityHighchartsApiService;

  @GetMapping(value = "/v1/highcharts/city/{city}/annual-addition-of-solar-installations", produces = "application/json")
  public Mono<SolarCityHighchartsResponse> getAnnualAdditionOfSolarInstallations(
      @PathVariable String city,
      @RequestParam(required = false) boolean previousSolarInstallationsOnly
  ) {
    return solarCityHighchartsApiService.createAnnualAdditionOfSolarInstallationsChart(
        city,
        previousSolarInstallationsOnly
    );
  }

  @PostMapping(value = "/v1/highcharts/create/temporary/annual-addition-of-solar-installations", produces = "application/json")
  public Mono<SolarCityHighchartsResponse> createTemporaryAnnualAdditionOfSolarInstallations(
      @RequestBody SolarCityRequest request
  ) {
    return solarCityHighchartsApiService.createTemporaryAnnualAdditionOfSolarInstallationsChart(
        request.getCityName(), request.getPostcodes(),
        request.getTotalSolarPotentialMWp(), request.getTotalSolarPotentialTargetYear()
    );
  }
}
