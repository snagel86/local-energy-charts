package de.local.energycharts.mastr.gateway;

import de.local.energycharts.core.model.SolarSystem;
import reactor.core.publisher.Flux;

public interface MastrGateway {

  Flux<SolarSystem> getSolarSystems(Integer postcode);
}
