package de.local.energycharts.solarcity.ports.out;

import de.local.energycharts.solarcity.model.SolarCity;
import org.jmolecules.architecture.hexagonal.SecondaryPort;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@SecondaryPort
public interface SolarCityRepository {

  Mono<SolarCity> save(SolarCity solarCity);

  Mono<SolarCity> findById(String id);

  SolarCity findByIdSync(String id);

  Mono<SolarCity> findByName(String name);

  Flux<SolarCity> findAll();
}
