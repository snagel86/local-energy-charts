package de.local.energycharts.solarcity.schedule;

import de.local.energycharts.solarcity.service.SolarCityUpdateWatchdogService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SolarCityUpdateWatchdogScheduler {

  private final SolarCityUpdateWatchdogService solarCityUpdateWatchdogService;

  @Scheduled(cron = "0 0 2 * * *")
  public void watch() {
    solarCityUpdateWatchdogService.sendErrorNotificationIfNoUpdateWithin24Hours()
        .subscribe();
  }
}
