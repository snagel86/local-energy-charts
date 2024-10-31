package de.local.energycharts.schedule;

import de.local.energycharts.solarcity.ports.in.SendErrorNotification;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SolarCityUpdateWatchdogScheduler {

  private final SendErrorNotification sendErrorNotification;

  @Scheduled(cron = "0 0 2 * * *")
  public void watch() {
    sendErrorNotification.sendWhenNoUpdateWithin24Hours()
        .subscribe();
  }
}
