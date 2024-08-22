package de.local.energycharts.api.v1.mail.controller;

import de.local.energycharts.api.v1.mail.model.MailRequest;
import de.local.energycharts.api.v1.mail.service.MailApiService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/v1")
@RequiredArgsConstructor
public class MailController {

  private final MailApiService mailApiService;

  @Operation(summary = "Sends an e-mail.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "mail successfully sent",
          content = {@Content(mediaType = "application/json")})
  })
  @PostMapping(value = "/mail/send", produces = "application/json")
  public Mono<Void> sendMail(@RequestBody MailRequest mailRequest) {
    return mailApiService.sendMail(mailRequest);
  }
}
