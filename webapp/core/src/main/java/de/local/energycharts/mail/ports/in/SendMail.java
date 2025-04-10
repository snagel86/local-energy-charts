package de.local.energycharts.mail.ports.in;

import de.local.energycharts.mail.model.Mail;
import org.jmolecules.architecture.hexagonal.PrimaryPort;
import reactor.core.publisher.Mono;

@PrimaryPort
public interface SendMail {

  Mono<Void> handle(Mail mail);
}
