package de.local.energycharts.api.v1.mailing.service;

import de.local.energycharts.api.v1.mailing.model.MailRequest;
import de.local.energycharts.api.v1.mailing.model.mapper.MailMapper;
import de.local.energycharts.mailing.service.MailService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class MailApiService {

  private final MailService mailService;
  private final MailMapper mailMapper;

  public Mono<Void> sendMail(MailRequest mailRequest) {
    var mail = mailMapper.map(mailRequest);
    return mailService.sendMail(mail);
  }
}
