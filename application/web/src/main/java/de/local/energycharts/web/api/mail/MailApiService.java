package de.local.energycharts.web.api.mail;

import de.local.energycharts.core.mail.ports.in.SendMail;
import de.local.energycharts.web.api.mail.model.MailRequest;
import de.local.energycharts.web.api.mail.model.mapper.MailMapper;
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
