package de.local.energycharts.mastr.schedule;

import de.local.energycharts.core.repository.SolarCityRepository;
import de.local.energycharts.core.service.SolarCityStatisticService;
import de.local.energycharts.mastr.service.MastrSolarService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MastrCrawler {

  private final SolarCityRepository solarCityRepository;
  private final MastrSolarService mastrSolarService;
  private final SolarCityStatisticService solarCityStatisticService;

  @Scheduled(cron = "0 0 2 * * *")
  public void updateAllSolarCities() {
    solarCityRepository.findAll()
        .flatMap(mastrSolarService::updateSolarCity)
        .map(updatedSolarCity -> solarCityStatisticService.resetCachedSolarCity(updatedSolarCity.getName()))
        .collectList().block();
  }
}
