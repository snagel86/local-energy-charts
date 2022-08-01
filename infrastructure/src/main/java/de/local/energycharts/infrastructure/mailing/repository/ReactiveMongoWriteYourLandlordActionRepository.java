package de.local.energycharts.infrastructure.mailing.repository;

import de.local.energycharts.mailing.landlord.model.WriteYourLandlordAction;
import de.local.energycharts.mailing.landlord.repository.WriteYourLandlordActionRepository;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReactiveMongoWriteYourLandlordActionRepository
    extends ReactiveMongoRepository<WriteYourLandlordAction, String>,
    WriteYourLandlordActionRepository {

}
