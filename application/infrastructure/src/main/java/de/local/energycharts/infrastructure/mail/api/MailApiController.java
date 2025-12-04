package de.local.energycharts.infrastructure.mail.api;

import de.local.energycharts.infrastructure.mail.api.model.MailRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.jmolecules.architecture.hexagonal.PrimaryAdapter;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@PrimaryAdapter
@Tag(name = "Mail")
@RestController
@RequestMapping("/v1")
@RequiredArgsConstructor
public class MailApiController {

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
