package de.local.energycharts.api.v1.solarcity.statistic.model.common.mapper;

import de.local.energycharts.api.v1.solarcity.statistic.model.common.SolarCityOverviewResponse;
import de.local.energycharts.solarcity.model.statistic.SolarCityOverview;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SolarCityOverviewMapper {

  SolarCityOverviewResponse mapToResponse(SolarCityOverview overview);
}
