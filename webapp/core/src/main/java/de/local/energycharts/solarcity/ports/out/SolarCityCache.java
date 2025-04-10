package de.local.energycharts.solarcity.ports.out;

import de.local.energycharts.solarcity.model.SolarCity;
import org.jmolecules.architecture.hexagonal.SecondaryPort;
import reactor.core.publisher.Mono;

@SecondaryPort
public interface SolarCityCache {

    Mono<SolarCity> get(String id);

    Mono<SolarCity> reset(SolarCity solarCity);

    Mono<SolarCity> cacheByName(SolarCity solarCity);

    boolean isAlreadyCached(String name);

    Mono<SolarCity> getByName(String name);
}
