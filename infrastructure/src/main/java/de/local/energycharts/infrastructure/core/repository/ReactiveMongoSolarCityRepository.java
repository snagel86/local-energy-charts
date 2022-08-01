package de.local.energycharts.infrastructure.core.repository;

import de.local.energycharts.core.model.SolarCity;
import de.local.energycharts.core.repository.SolarCityRepository;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface ReactiveMongoSolarCityRepository
    extends ReactiveMongoRepository<SolarCity, String>, SolarCityRepository {

  Mono<SolarCity> findByName(String name);
}
