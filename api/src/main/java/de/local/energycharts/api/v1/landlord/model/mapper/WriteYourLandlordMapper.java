package de.local.energycharts.api.v1.landlord.model.mapper;

import de.local.energycharts.api.v1.landlord.model.WriteYourLandlordRequest;
import de.local.energycharts.api.v1.landlord.model.WriteYourLandlordStatisticResponse;
import de.local.energycharts.mailing.landlord.model.WriteYourLandlord;
import de.local.energycharts.mailing.landlord.model.WriteYourLandlordStatistic;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface WriteYourLandlordMapper {

  WriteYourLandlord mapToBusinessModel(WriteYourLandlordRequest request);

  WriteYourLandlordStatisticResponse mapToResponse(WriteYourLandlordStatistic statistic);
}
