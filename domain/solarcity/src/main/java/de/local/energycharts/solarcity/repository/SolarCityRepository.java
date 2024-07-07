package de.local.energycharts.solarcity.repository;

import de.local.energycharts.solarcity.model.SolarCity;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface SolarCityRepository {

  Mono<SolarCity> save(SolarCity solarCity);

  Mono<SolarCity> findByName(String name);

  SolarCity findByNameSync(String name);

  Flux<SolarCity> findAll();
}
