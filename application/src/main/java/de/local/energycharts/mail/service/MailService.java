package de.local.energycharts.mail.service;

import de.local.energycharts.mail.model.Mail;
import de.local.energycharts.mail.port.MailSenderGateway;
import de.local.energycharts.mail.usecase.SendMail;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class MailService implements SendMail {

  private final MailSenderGateway mailSenderGateway;

  @SneakyThrows
  public Mono<Void> sendMail(Mail mail) {
    mailSenderGateway.sendMail(mail);
    return Mono.when().then();
  }
}
