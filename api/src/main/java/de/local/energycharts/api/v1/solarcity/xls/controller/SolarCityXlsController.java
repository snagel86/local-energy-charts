package de.local.energycharts.api.v1.solarcity.xls.controller;

import de.local.energycharts.api.v1.solarcity.xls.service.SolarCityXlsApiService;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@Tag(name = "Solar City")
@RestController
@RequestMapping("/v1/solar-cities")
@RequiredArgsConstructor
@Hidden
public class SolarCityXlsController {

    private final SolarCityXlsApiService solarCityXlsApiService;

    @GetMapping(value = "/{solarCityName}/solar-systems/xls", produces = "application/json")
    public Mono<ResponseEntity<?>> getSolarSystemsXLS(@PathVariable("solarCityName") String solarCityName) {
        return solarCityXlsApiService.getSolarSystemsXls(solarCityName);
    }
}
