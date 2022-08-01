package de.local.energycharts.core.repository;

import de.local.energycharts.core.model.SolarCity;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface SolarCityRepository {

  <S extends SolarCity> Mono<SolarCity> save(SolarCity entity);

  Mono<SolarCity> findByName(String name);

  Mono<SolarCity> findByIdOrName(String id, String name);

  Flux<SolarCity> findAll();
}
