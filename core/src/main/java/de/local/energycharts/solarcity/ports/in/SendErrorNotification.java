package de.local.energycharts.solarcity.ports.in;

import de.local.energycharts.solarcity.model.SolarCity;
import reactor.core.publisher.Flux;

public interface SendErrorNotification {

  Flux<SolarCity> sendWhenNoUpdateWithin24Hours();
}
