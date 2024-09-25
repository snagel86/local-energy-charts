package de.local.energycharts.solarcity.schedule;

import de.local.energycharts.solarcity.service.SolarCityService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

import static java.math.RoundingMode.HALF_UP;

@Component
@RequiredArgsConstructor
public class SolarCityUpdateScheduler {

  private final SolarCityService solarCityService;
  private final Logger logger = LoggerFactory.getLogger(SolarCityUpdateScheduler.class.getName());

  @Scheduled(cron = "0 0 */4 * * *")
  public void updateAllSolarCities() {
    solarCityService.updateAllSolarCities()
        .onErrorContinue((err, i) -> logger.error(err.getMessage()))
        .elapsed()
        .subscribe(updatedSolarCityTuple -> logger.info(
            "solar-city {} was updated in {} seconds.",
            updatedSolarCityTuple.getT2().getName(),
            BigDecimal.valueOf(updatedSolarCityTuple.getT1() / 1000.0).setScale(2, HALF_UP)
        ));
  }
}
