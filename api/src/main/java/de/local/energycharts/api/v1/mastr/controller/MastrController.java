package de.local.energycharts.api.v1.mastr.controller;

import de.local.energycharts.api.v1.mastr.model.CreateSolarCityRequest;
import de.local.energycharts.api.v1.mastr.model.SolarCityCreatedResponse;
import de.local.energycharts.api.v1.mastr.service.MastrApiService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
public class MastrController {

  private final MastrApiService mastrApiService;

  @PostMapping(value = "/v1/create/solar-city", produces = "application/json")
  public Mono<SolarCityCreatedResponse> createSolarCity(@RequestBody CreateSolarCityRequest request) {
    return mastrApiService.createSolarCity(request);
  }
}
