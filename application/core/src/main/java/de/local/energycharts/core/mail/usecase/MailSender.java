package de.local.energycharts.core.mail.usecase;

import de.local.energycharts.core.mail.model.Mail;
import de.local.energycharts.core.mail.ports.in.SendMail;
import de.local.energycharts.core.mail.ports.out.MailSenderGateway;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class MailSender implements SendMail {

  private final MailSenderGateway mailSenderGateway;

  @SneakyThrows
  public Mono<Void> handle(Mail mail) {
    mailSenderGateway.sendMail(mail);
    return Mono.when().then();
  }
}
