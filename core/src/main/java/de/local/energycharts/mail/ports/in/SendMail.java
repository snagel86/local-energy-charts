package de.local.energycharts.mail.ports.in;

import de.local.energycharts.mail.model.Mail;
import reactor.core.publisher.Mono;

public interface SendMail {

  Mono<Void> handle(Mail mail);
}
