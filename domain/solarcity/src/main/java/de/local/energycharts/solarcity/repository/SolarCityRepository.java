package de.local.energycharts.solarcity.repository;

import de.local.energycharts.solarcity.model.SolarCity;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface SolarCityRepository {

  <S extends SolarCity> Mono<SolarCity> save(SolarCity entity);

  Mono<SolarCity> findByName(String name);

  Mono<SolarCity> findByIdOrName(String id, String name);
  Mono<SolarCity> findByMunicipalityKey(String municipalityKey);

  Flux<SolarCity> findAll();
}
