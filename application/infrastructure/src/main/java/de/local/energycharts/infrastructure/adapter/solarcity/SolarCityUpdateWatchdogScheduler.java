package de.local.energycharts.infrastructure.adapter.solarcity;

import de.local.energycharts.core.solarcity.ports.in.SendErrorNotification;
import lombok.RequiredArgsConstructor;
import org.jmolecules.architecture.hexagonal.SecondaryAdapter;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@SecondaryAdapter
@Component
@RequiredArgsConstructor
public class SolarCityUpdateWatchdogScheduler {

  private final SendErrorNotification sendErrorNotification;

  @Scheduled(cron = "0 0 2 * * *")
  public void watch() {
    sendErrorNotification.noUpdateWithin24Hours()
        .subscribe();
  }
}
