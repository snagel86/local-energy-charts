package de.local.energycharts.mailing.contact.service;

import static java.time.Instant.now;

import de.local.energycharts.mailing.contact.model.Contact;
import de.local.energycharts.mailing.contact.model.ContactSuccess;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class ContactService {

  private final JavaMailSender mailSender;
  private final SimpleMailMessage defaultMailMessage;

  public Mono<ContactSuccess> sendContactMessage(Contact contact) {
    defaultMailMessage.setFrom(contact.getEmail());
    defaultMailMessage.setSubject("contact from " + contact.getName() + " via pv-frankfurt.de");
    defaultMailMessage.setText(contact.getMessage());
    mailSender.send(defaultMailMessage);

    return Mono.just(ContactSuccess.builder()
        .sent(contact.getMessage())
        .at(now())
        .to(contact.getEmail()).build()
    );
  }
}
