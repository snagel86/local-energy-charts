package de.local.energycharts.infrastructure.solarcity.repository;

import de.local.energycharts.infrastructure.solarcity.model.MongoSolarCity;
import de.local.energycharts.infrastructure.solarcity.model.mapper.MongoSolarCityMapper;
import de.local.energycharts.solarcity.model.SolarCity;
import de.local.energycharts.solarcity.repository.SolarCityRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.Duration;

import static de.local.energycharts.solarcity.model.Time.now;
import static java.math.RoundingMode.HALF_UP;
import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

@Repository
@RequiredArgsConstructor
public class MongoSolarCityRepository implements SolarCityRepository {

  private final MongoTemplate mongoTemplate;
  private final MongoSolarCityMapper solarCityMapper;
  private final Logger logger = LoggerFactory.getLogger(MongoSolarCityRepository.class);

  public Mono<SolarCity> save(SolarCity solarCity) {
    var start = now();
    var mongoSolarCity = solarCityMapper.mapToMongoDocument(solarCity);
    var savedSolarCity = mongoTemplate.save(mongoSolarCity);
    logger.info(
        "solar-city {} was saved in {} seconds",
        solarCity.getName(),
        BigDecimal.valueOf(Duration.between(start, now()).toMillis() / 1000.0).setScale(2, HALF_UP)
    );
    return Mono.just(solarCityMapper.mapToDomainModel(savedSolarCity));
  }

  public Mono<SolarCity> findByName(String name) {
    var solarCity = mongoTemplate.findOne(query(where("name").is(name)), MongoSolarCity.class);
    if (solarCity == null) {
      return Mono.empty();
    }
    return Mono.just(solarCityMapper.mapToDomainModel(solarCity));
  }

  public SolarCity findByNameSync(String name) {
    var solarCity = mongoTemplate.findOne(query(where("name").is(name)), MongoSolarCity.class);
    return solarCityMapper.mapToDomainModel(solarCity);
  }

  public Flux<SolarCity> findAll() {
    var query = new Query();
    query.fields().include(
        "id",
        "name",
        "municipalityKey",
        "created", "updated",
        "entireSolarPotentialOnRooftopsMWp", "targetYear"
    );

    return Flux.fromIterable(solarCityMapper
        .mapToDomainModels(mongoTemplate
            .find(query, MongoSolarCity.class))
    );
  }
}
