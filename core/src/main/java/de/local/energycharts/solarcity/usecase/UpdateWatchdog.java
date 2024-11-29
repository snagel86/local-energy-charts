package de.local.energycharts.solarcity.usecase;

import de.local.energycharts.mail.model.Mail;
import de.local.energycharts.mail.ports.out.MailSenderGateway;
import de.local.energycharts.solarcity.model.SolarCity;
import de.local.energycharts.solarcity.ports.in.SendErrorNotification;
import de.local.energycharts.solarcity.ports.out.SolarCityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import static java.time.temporal.ChronoUnit.HOURS;

@Service
@RequiredArgsConstructor
public class UpdateWatchdog implements SendErrorNotification {

  @Value("${notification.mail.to}")
  private String notificationMailTo;
  private final MailSenderGateway notificationMailSender;
  private final SolarCityRepository solarCityRepository;

  public Flux<SolarCity> whenNoUpdateWithin24Hours() {
    return solarCityRepository.findAll()
        .filter(solarCity -> solarCity.wasNotUpdatedWithin(24, HOURS))
        .map(solarCity -> {
          var errorNotificationMail = Mail.builder()
              .to(notificationMailTo)
              .subject(String.format("Die Solarstadt %s wurde nicht innerhalb eines Tages aktualisiert!", solarCity.getName()))
              .message("Bitte überprüfen!").build();
          notificationMailSender.sendMail(errorNotificationMail);
          return solarCity;
        });
  }
}
