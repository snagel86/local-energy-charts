package de.local.energycharts.mail.usecase;

import de.local.energycharts.mail.model.Mail;
import reactor.core.publisher.Mono;

public interface SendMail {

  Mono<Void> sendMail(Mail mail);
}
