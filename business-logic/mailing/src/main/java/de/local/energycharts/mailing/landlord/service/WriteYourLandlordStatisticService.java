package de.local.energycharts.mailing.landlord.service;

import de.local.energycharts.mailing.landlord.model.WriteYourLandlordStatistic;
import de.local.energycharts.mailing.landlord.repository.WriteYourLandlordActionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class WriteYourLandlordStatisticService {

  private final WriteYourLandlordActionRepository actionRepository;

  public Mono<WriteYourLandlordStatistic> createStatistic() {
    return actionRepository.findAll()
        .collectList()
        .map(actions -> WriteYourLandlordStatistic.builder()
            .pageOpened(actions.stream().filter(action -> action.getPageOpenedAt() != null).count())
            .mailSent(actions.stream().filter(action -> action.getMailSentAt() != null).count())
            .build()
        );
  }
}
