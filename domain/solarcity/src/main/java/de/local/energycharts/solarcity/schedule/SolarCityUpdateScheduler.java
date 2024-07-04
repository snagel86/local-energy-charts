package de.local.energycharts.solarcity.schedule;

import de.local.energycharts.solarcity.service.SolarCityUpdateService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import static de.local.energycharts.solarcity.model.Time.now;

@Component
@RequiredArgsConstructor
public class SolarCityUpdateScheduler {

  private final SolarCityUpdateService solarCityUpdateService;
  private final Logger logger = LoggerFactory.getLogger(SolarCityUpdateScheduler.class.getName());

  @Scheduled(cron = "0 0 */4 * * *")
  public void updateAllSolarCities() {
    final var start = now();
    solarCityUpdateService.updateAllSolarCities()
        .onErrorContinue((err, i) -> logger.error(err.getMessage()))
        .subscribe(updatedSolarCity -> logger.info("solar-city '{}' was updated in {} ms.",
                updatedSolarCity.getName(), (now().toEpochMilli() - start.toEpochMilli())
            )
        );
  }
}
