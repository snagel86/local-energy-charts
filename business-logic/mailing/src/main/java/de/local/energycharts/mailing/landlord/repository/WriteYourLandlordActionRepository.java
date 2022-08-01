package de.local.energycharts.mailing.landlord.repository;

import de.local.energycharts.mailing.landlord.model.WriteYourLandlordAction;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface WriteYourLandlordActionRepository {

  <S extends WriteYourLandlordAction> Mono<WriteYourLandlordAction> save(WriteYourLandlordAction entity);

  Flux<WriteYourLandlordAction> findAll();

  Mono<Void> deleteAll();
}
