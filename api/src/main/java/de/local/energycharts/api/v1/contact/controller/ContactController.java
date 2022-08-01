package de.local.energycharts.api.v1.contact.controller;

import de.local.energycharts.api.v1.contact.model.ContactRequest;
import de.local.energycharts.api.v1.contact.model.ContactSuccessResponse;
import de.local.energycharts.api.v1.contact.service.ContactApiService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
public class ContactController {

  private final ContactApiService contactApiService;

  @PostMapping(value = "/v1/contact", produces = "application/json")
  public Mono<ContactSuccessResponse> sendContactMessage(@RequestBody ContactRequest request) {
    return contactApiService.sendContactMessage(request);
  }
}
