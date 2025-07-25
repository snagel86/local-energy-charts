package de.local.energycharts.api.v1.solarcity.admin;

import de.local.energycharts.api.v1.solarcity.admin.model.CreateSolarCityRequest;
import de.local.energycharts.api.v1.solarcity.admin.model.SolarCityCreatedResponse;
import de.local.energycharts.api.v1.solarcity.admin.model.SolarCityResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.jmolecules.architecture.hexagonal.PrimaryAdapter;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@PrimaryAdapter
@Tag(name = "Solar City")
@RestController
@RequestMapping("/v1")
@RequiredArgsConstructor
public class SolarCityApiController {

  private final SolarCityApiService solarCityApiService;

  @Operation(summary = """
      Creates a new or replaces an existing solar city based on the solar systems
      registered in the Marktstammdatenregister.
      You can optionally enter the municipality key of the city,
      which you can find here: https://www.statistikportal.de/de/gemeindeverzeichnis.
      Alternatively, all available postal codes are resolved via Opendatasoft using the name of the city,
      which is then used to query the corresponding solar installations from the Marktstammdatenregister.
      A schedule then takes over the updating so that the data is always up to date.
      """
  )
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Responds with statistics how many solar systems are already installed.",
          content = {@Content(mediaType = "application/json",
              schema = @Schema(implementation = SolarCityCreatedResponse.class))})
  })
  @PutMapping(value = "/solar-city/create", produces = "application/json")
  public Mono<SolarCityCreatedResponse> createOrReplaceSolarCity(@RequestBody CreateSolarCityRequest request) {
    return solarCityApiService.createOrUpdateSolarCity(request);
  }

  @Operation(summary = "Lists all created solar cities.")
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
  @GetMapping(value = "/solar-cities/{id}/postcodes", produces = "application/json")
  public Flux<Integer> getAllPostcodes(@PathVariable("id") String id) {
    return solarCityApiService.getAllPostcodes(id);
  }

  @Operation(hidden = true)
  @DeleteMapping(value = "/solar-cities/{id}", produces = "application/json")
  public void delete(@PathVariable("id") String id) {
    solarCityApiService.delete(id);
  }
}
