package de.local.energycharts.api.v1.landlord.controller;

import de.local.energycharts.api.v1.landlord.model.WriteYourLandlordRequest;
import de.local.energycharts.api.v1.landlord.model.WriteYourLandlordStatisticResponse;
import de.local.energycharts.api.v1.landlord.service.WriteYourLandlordApiService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
public class WriteYourLandlordController {

  private final WriteYourLandlordApiService writeYourLandlordApiService;

  @PostMapping(value = "/v1/write-your-landlord/opened", produces = "application/json")
  public Mono<Void> writeYourLandlordPageWasOpened() {
    return writeYourLandlordApiService.createActionThatThePageWasOpened();
  }

  @PostMapping(value = "/v1/write-your-landlord/send", produces = "application/json")
  public Mono<Void> writeYourLandlord(@RequestBody WriteYourLandlordRequest request) {
    return writeYourLandlordApiService.sendMail(request);
  }

  @GetMapping(value = "/v1/write-your-landlord/statistic", produces = "application/json")
  public Mono<WriteYourLandlordStatisticResponse> getStatistic() {
    return writeYourLandlordApiService.createStatistic();
  }

  @DeleteMapping(value = "/v1/write-your-landlord/reset", produces = "application/json")
  public Mono<Void> resetWriteYourLandlord() {
    return writeYourLandlordApiService.deleteAllSavedActions();
  }
}
