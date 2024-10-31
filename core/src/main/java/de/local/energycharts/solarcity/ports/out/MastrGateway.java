package de.local.energycharts.solarcity.ports.out;

import de.local.energycharts.solarcity.model.SolarSystem;
import reactor.core.publisher.Flux;

/**
 * @see <a href="https://www.marktstammdatenregister.de/MaStR">MaStR</a>
 */
public interface MastrGateway {

  Flux<SolarSystem> getSolarSystemsByPostcode(Integer postcode);
  Flux<SolarSystem> getSolarSystemsByMunicipalityKey(String municipalityKey);
}
