package de.local.energycharts.solarcity.usecase;

import de.local.energycharts.solarcity.model.SolarCity;
import reactor.core.publisher.Mono;

public interface CreateOrUpdateSolarCity {

  Mono<SolarCity> createOrUpdateSolarCity(
      String name,
      Double entireSolarPotentialOnRooftopsMWp, Integer targetYear
  );

  Mono<SolarCity> createOrUpdateSolarCity(
      String name,
      String municipalityKey,
      Double entireSolarPotentialOnRooftopsMWp, Integer targetYear
  );

  Mono<SolarCity> createSolarCityTemporary(
      String name,
      Double entireSolarPotentialOnRooftopsMWp, Integer targetYear
  );

  Mono<SolarCity> updateSolarCity(SolarCity solarCity);
}
