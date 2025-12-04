package de.local.energycharts.core.solarcity.usecase;

import de.local.energycharts.core.mail.model.Mail;
import de.local.energycharts.core.mail.ports.out.MailSenderGateway;
import de.local.energycharts.core.solarcity.model.SolarCity;
import de.local.energycharts.core.solarcity.model.Time;
import de.local.energycharts.core.solarcity.ports.out.SolarCityRepository;
import de.local.energycharts.core.solarcity.usecase.UpdateWatchdog;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;

import java.time.Instant;

import static de.local.energycharts.core.solarcity.model.Time.now;
import static java.time.temporal.ChronoUnit.HOURS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UpdateWatchdogTest {

  @InjectMocks
  private UpdateWatchdog updateWatchdog;
  @Mock
  private SolarCityRepository solarCityRepository;
  @Mock
  private MailSenderGateway notificationMailSender;

  @Test
  void send_error_notification_when_no_update_within_24_hours() {
    Time.freezeNowAt(Instant.parse("2024-01-01T00:00:00.00Z"));
    var frankfurt = SolarCity.createNewSolarCity("Frankfurt am Main")
        .setId("1")
        .setUpdated(now().minus(23, HOURS));
    var koeln = SolarCity.createNewSolarCity("Köln")
        .setId("4")
        .setUpdated(now().minus(25, HOURS));
    when(solarCityRepository.findAll()).thenReturn(Flux.just(koeln, frankfurt));

    assertThat(updateWatchdog.noUpdateWithin24Hours().collectList().block())
        .containsOnly(koeln);
    verify(notificationMailSender, times(1)).sendMail(any(Mail.class));
  }
}