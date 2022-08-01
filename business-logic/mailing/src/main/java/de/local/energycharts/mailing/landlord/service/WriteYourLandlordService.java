package de.local.energycharts.mailing.landlord.service;

import static java.time.Instant.now;

import de.local.energycharts.mailing.landlord.model.WriteYourLandlord;
import de.local.energycharts.mailing.landlord.model.WriteYourLandlordAction;
import de.local.energycharts.mailing.landlord.repository.WriteYourLandlordActionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class WriteYourLandlordService {

  private final WriteYourLandlordActionRepository actionRepository;
  private final JavaMailSender mailSender;

  public Mono<WriteYourLandlordAction> createActionThatThePageWasOpened() {
    return actionRepository.save(new WriteYourLandlordAction().setPageOpenedAt(now()));
  }

  public Mono<WriteYourLandlordAction> sendMail(WriteYourLandlord writeYourLandlord) {
    sendMailToLandlord(writeYourLandlord);
    return saveActionThatMailWasSent();
  }

  private void sendMailToLandlord(WriteYourLandlord writeYourLandlord) {
    SimpleMailMessage message = new SimpleMailMessage();
    message.setFrom(writeYourLandlord.getFromTenant());
    message.setTo(writeYourLandlord.getToLandlord());
    message.setSubject("Bitte verpachten Sie das Dach für eine Solaranlage an eine Energiegenossenschaft");
    message.setText(writeYourLandlord.getMessage());
    mailSender.send(message);
  }

  private Mono<WriteYourLandlordAction> saveActionThatMailWasSent() {
    return actionRepository.save(new WriteYourLandlordAction().setMailSentAt(now()));
  }

  public Mono<Void> deleteAllSavedActions() {
    return actionRepository.deleteAll();
  }
}
