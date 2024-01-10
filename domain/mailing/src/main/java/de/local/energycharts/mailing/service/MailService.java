package de.local.energycharts.mailing.service;

import de.local.energycharts.mailing.gateway.MailSenderGateway;
import de.local.energycharts.mailing.model.Mail;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class MailService {

  private final MailSenderGateway mailSenderGateway;

  @SneakyThrows
  public Mono<Void> sendMail(Mail mail) {
    mailSenderGateway.sendMail(mail);
    return Mono.when().then();
  }
}
