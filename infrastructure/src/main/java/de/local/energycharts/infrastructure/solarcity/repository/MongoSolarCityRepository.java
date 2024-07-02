package de.local.energycharts.infrastructure.solarcity.repository;

import de.local.energycharts.infrastructure.solarcity.model.MongoSolarCity;
import de.local.energycharts.infrastructure.solarcity.model.mapper.MongoSolarCityMapper;
import de.local.energycharts.solarcity.model.SolarCity;
import de.local.energycharts.solarcity.repository.SolarCityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

@Repository
@RequiredArgsConstructor
public class MongoSolarCityRepository implements SolarCityRepository {

  private final MongoTemplate mongoTemplate;
  private final MongoSolarCityMapper solarCityMapper;

  public Mono<SolarCity> save(SolarCity solarCity) {
    var mongoSolarCity = solarCityMapper.mapToMongoDocument(solarCity);
    return Mono.just(solarCityMapper.mapToDomainModel(
        mongoTemplate.save(mongoSolarCity)
    ));
  }

  public Mono<SolarCity> findByName(String name) {
    var solarCity = mongoTemplate.findOne(query(where("name").is(name)), MongoSolarCity.class);
    if (solarCity == null) {
      return Mono.empty();
    }
    return Mono.just(solarCityMapper.mapToDomainModel(solarCity));
  }

  public Flux<SolarCity> findAll() {
    return Flux.fromIterable(solarCityMapper
        .mapToDomainModels(mongoTemplate
            .findAll(MongoSolarCity.class)
        ));
  }
}
