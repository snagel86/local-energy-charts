package de.local.energycharts.api.v1.solarcity.create.controller;

import de.local.energycharts.api.v1.solarcity.create.model.CreateSolarCityRequest;
import de.local.energycharts.api.v1.solarcity.create.model.SolarCityCreatedResponse;
import de.local.energycharts.api.v1.solarcity.create.model.SolarCityResponse;
import de.local.energycharts.api.v1.solarcity.create.service.SolarCityApiService;
import de.local.energycharts.solarcity.model.SolarCity;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Tag(name = "Solar City")
@RestController
@RequestMapping("/v1")
@RequiredArgsConstructor
public class SolarCityController {

  private final SolarCityApiService solarCityApiService;

  @Operation(summary = "Creates a solar city based on the solar installations registered in the Marktstammdatenregister. " +
      "Using Opendatasoft, all available post-codes are resolved by the name of the city, " +
      "which then can be used to request the corresponding solar installations from the Marktstammdatenregister. " +
      "Afterwards, a schedule takes over the update, so that the data always remains up-to-date on a daily basis."
  )
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Responds with statistics how many solar systems are already installed.",
          content = {@Content(mediaType = "application/json",
              schema = @Schema(implementation = SolarCityCreatedResponse.class))})
  })
  @PostMapping(value = "/solar-city/create", produces = "application/json")
  public Mono<SolarCityCreatedResponse> createSolarCity(@RequestBody CreateSolarCityRequest request) {
    return solarCityApiService.createSolarCity(request);
  }

  @Operation(summary = "List all created solar cities.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "List all created solar cities.",
          content = {@Content(mediaType = "application/json",
              schema = @Schema(implementation = SolarCityResponse[].class))})
  })
  @GetMapping(value = "/solar-cities/all", produces = "application/json")
  public Flux<SolarCityResponse> getAllSolarCities() {
    return solarCityApiService.getAllSolarCities();
  }

  @Operation(summary = "Checks the resolved post-codes via Opendatasoft by the given name of the created solar city.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "resolved zip codes",
          content = {@Content(mediaType = "application/json",
              schema = @Schema(implementation = Integer[].class))})
  })
  @GetMapping(value = "/solar-cities/{solarCityName}/postcodes", produces = "application/json")
  public Flux<Integer> getAllPostcodes(@PathVariable("solarCityName") String solarCityName) {
    return solarCityApiService.getAllPostcodes(solarCityName);
  }
}
