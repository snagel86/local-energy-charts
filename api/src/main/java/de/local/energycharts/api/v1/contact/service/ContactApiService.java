package de.local.energycharts.api.v1.contact.service;

import de.local.energycharts.api.v1.contact.model.ContactRequest;
import de.local.energycharts.api.v1.contact.model.ContactSuccessResponse;
import de.local.energycharts.api.v1.contact.model.mapper.ContactMapper;
import de.local.energycharts.mailing.contact.service.ContactService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class ContactApiService {

  private final ContactService contactService;
  private final ContactMapper contactMapper;

  public Mono<ContactSuccessResponse> sendContactMessage(ContactRequest contactRequest) {
    var contact = contactMapper.mapToBusinessModel(contactRequest);
    return contactService.sendContactMessage(contact)
        .map(contactMapper::mapToResponse);
  }
}
