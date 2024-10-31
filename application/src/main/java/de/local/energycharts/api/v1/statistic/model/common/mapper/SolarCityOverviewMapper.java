package de.local.energycharts.api.v1.statistic.model.common.mapper;

import de.local.energycharts.api.v1.statistic.model.common.SolarCityOverviewResponse;
import de.local.energycharts.statistic.model.SolarCityOverview;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SolarCityOverviewMapper {

  SolarCityOverviewResponse mapToResponse(SolarCityOverview overview);
}
