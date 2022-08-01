package de.local.energycharts.mastr.service;

import static java.time.Instant.now;

import de.local.energycharts.core.model.District;
import de.local.energycharts.core.model.SolarCity;
import de.local.energycharts.core.repository.SolarCityRepository;
import de.local.energycharts.mastr.gateway.MastrGateway;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class MastrSolarService {

  private final SolarCityRepository solarCityRepository;
  private final MastrGateway mastrGateway;

  public Mono<SolarCity> createOrUpdateSolarCity(
      String cityName, Set<Integer> postcodes,
      Double totalSolarPotentialMWp, Integer targetYear
  ) {
    return solarCityRepository.findByName(cityName)
        .defaultIfEmpty(SolarCity.createNewSolarCity(cityName))
        .map(solarCity -> solarCity
            .setUpdated(now())
            .setTotalSolarPotentialMWp(totalSolarPotentialMWp)
            .setTargetYear(targetYear)
        )
        .flatMap(solarCity -> Flux.fromIterable(postcodes)
            .flatMap(postcode ->
                mastrGateway.getSolarSystems(postcode).collect(Collectors.toSet())
                    .map(solarSystems ->
                        District.builder()
                            .postcode(postcode)
                            .solarSystems(solarSystems).build())
            ).collect(Collectors.toSet())
            .map(solarCity::setDistricts)
            .flatMap(solarCityRepository::save)
        );
  }

  public Mono<SolarCity> updateSolarCity(SolarCity solarCity) {
    return createOrUpdateSolarCity(
        solarCity.getName(),
        solarCity.getDistricts().stream().map(District::getPostcode).collect(Collectors.toSet()),
        solarCity.getTotalSolarPotentialMWp(),
        solarCity.getTargetYear()
    );
  }

  public Mono<SolarCity> createSolarCityTemporary(
      String cityName, Set<Integer> postcodes,
      Double totalSolarPotentialMWp, Integer totalSolarPotentialTargetYear
  ) {
    return Mono.just(
            SolarCity.createNewSolarCity(cityName)
                .setTotalSolarPotentialMWp(totalSolarPotentialMWp)
                .setTargetYear(totalSolarPotentialTargetYear)
        )
        .flatMap(solarCity -> Flux.fromIterable(postcodes)
            .flatMap(postcode ->
                mastrGateway.getSolarSystems(postcode).collect(Collectors.toSet())
                    .map(solarSystems ->
                        District.builder()
                            .postcode(postcode)
                            .solarSystems(solarSystems).build())
            ).collect(Collectors.toSet())
            .map(solarCity::setDistricts)
        );
  }
}
