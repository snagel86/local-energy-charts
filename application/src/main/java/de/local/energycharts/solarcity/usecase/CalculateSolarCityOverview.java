package de.local.energycharts.solarcity.usecase;

import de.local.energycharts.solarcity.model.statistic.SolarCityOverview;
import reactor.core.publisher.Mono;

public interface CalculateSolarCityOverview {

  Mono<SolarCityOverview> calculateSolarCityOverview(String id);
}
