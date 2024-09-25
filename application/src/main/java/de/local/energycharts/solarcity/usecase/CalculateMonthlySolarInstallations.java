package de.local.energycharts.solarcity.usecase;

import de.local.energycharts.solarcity.model.statistic.MonthlySolarInstallations;
import reactor.core.publisher.Mono;

public interface CalculateMonthlySolarInstallations {

  Mono<MonthlySolarInstallations> calculateMonthlySolarInstallations(String id);
}
