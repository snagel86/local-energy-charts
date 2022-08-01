package de.local.energycharts.api.v1.statistic.model.mapper;

import de.local.energycharts.core.model.statistic.AdditionOfSolarInstallations;
import de.local.energycharts.api.v1.statistic.model.AdditionOfSolarInstallationsResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AdditionOfSolarInstallationsMapper {

  AdditionOfSolarInstallationsResponse mapToResponse(AdditionOfSolarInstallations additions);
}
