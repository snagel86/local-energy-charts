package de.local.energycharts.infrastructure.mail.api;

import de.local.energycharts.infrastructure.mail.api.model.MailRequest;
import de.local.energycharts.infrastructure.mail.api.model.mapper.MailMapper;
import de.local.energycharts.core.mail.ports.in.SendMail;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class MailApiService {

  private final SendMail sendMail;
  private final MailMapper mailMapper;

  public Mono<Void> sendMail(MailRequest mailRequest) {
    var mail = mailMapper.map(mailRequest);
    return sendMail.handle(mail);
  }
}
