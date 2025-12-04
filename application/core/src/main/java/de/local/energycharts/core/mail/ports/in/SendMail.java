package de.local.energycharts.core.mail.ports.in;

import de.local.energycharts.core.mail.model.Mail;
import org.jmolecules.architecture.hexagonal.PrimaryPort;
import reactor.core.publisher.Mono;

@PrimaryPort
public interface SendMail {

  Mono<Void> handle(Mail mail);
}
