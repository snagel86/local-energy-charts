package de.local.energycharts.solarcity.ports.in;

import de.local.energycharts.solarcity.model.SolarCity;
import org.jmolecules.architecture.hexagonal.PrimaryPort;
import reactor.core.publisher.Flux;

@PrimaryPort
public interface SendErrorNotification {

  Flux<SolarCity> noUpdateWithin24Hours();
}
