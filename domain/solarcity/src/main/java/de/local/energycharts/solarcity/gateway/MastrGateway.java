package de.local.energycharts.solarcity.gateway;

import de.local.energycharts.solarcity.model.SolarSystem;
import reactor.core.publisher.Flux;

/**
 * @see <a href="https://www.marktstammdatenregister.de/MaStR">MaStR</a>
 */
public interface MastrGateway {

  Flux<SolarSystem> getSolarSystems(Integer postcode);
}
