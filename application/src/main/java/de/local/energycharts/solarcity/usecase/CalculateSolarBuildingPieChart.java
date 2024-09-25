package de.local.energycharts.solarcity.usecase;

import de.local.energycharts.solarcity.model.statistic.SolarBuildingPieChart;
import reactor.core.publisher.Mono;

public interface CalculateSolarBuildingPieChart {

  Mono<SolarBuildingPieChart> calculateSolarBuildingPieChart(String id);
}
