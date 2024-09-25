package de.local.energycharts.solarcity.usecase;

import de.local.energycharts.solarcity.model.statistic.AnnualSolarInstallations;
import reactor.core.publisher.Mono;

public interface CalculateAnnualSolarInstallations {

  Mono<AnnualSolarInstallations> calculateAnnualSolarInstallations(String id);
}
