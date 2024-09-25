package de.local.energycharts.solarcity.usecase;

import de.local.energycharts.solarcity.model.SolarCity;
import reactor.core.publisher.Flux;

public interface GetAllSolarCities {

  Flux<SolarCity> getAllSolarCities();
}
