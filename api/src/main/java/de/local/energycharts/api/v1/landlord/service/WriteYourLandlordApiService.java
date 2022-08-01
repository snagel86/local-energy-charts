package de.local.energycharts.api.v1.landlord.service;

import de.local.energycharts.api.v1.landlord.model.WriteYourLandlordRequest;
import de.local.energycharts.api.v1.landlord.model.WriteYourLandlordStatisticResponse;
import de.local.energycharts.api.v1.landlord.model.mapper.WriteYourLandlordMapper;
import de.local.energycharts.mailing.landlord.service.WriteYourLandlordService;
import de.local.energycharts.mailing.landlord.service.WriteYourLandlordStatisticService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class WriteYourLandlordApiService {

  private final WriteYourLandlordService writeYourLandlordService;
  private final WriteYourLandlordStatisticService writeYourLandlordStatisticService;
  private final WriteYourLandlordMapper writeYourLandlordMapper;

  public Mono<Void> createActionThatThePageWasOpened() {
    return writeYourLandlordService
        .createActionThatThePageWasOpened()
        .flatMap(action -> Mono.just(action).then());
  }

  public Mono<Void> sendMail(WriteYourLandlordRequest request) {
    var writeYourLandlord = writeYourLandlordMapper.mapToBusinessModel(request);
    return writeYourLandlordService
        .sendMail(writeYourLandlord)
        .flatMap(action -> Mono.just(action).then());
  }

  public Mono<WriteYourLandlordStatisticResponse> createStatistic() {
    return writeYourLandlordStatisticService.createStatistic()
        .map(writeYourLandlordMapper::mapToResponse);
  }

  public Mono<Void> deleteAllSavedActions() {
    return writeYourLandlordService.deleteAllSavedActions();
  }
}
