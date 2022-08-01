package de.local.energycharts.api.v1.statistic.model.mapper;

import de.local.energycharts.api.v1.statistic.model.SolarCityOverviewResponse;
import de.local.energycharts.core.model.statistic.SolarCityOverview;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SolarCityOverviewMapper {

  SolarCityOverviewResponse mapToResponse(SolarCityOverview overview);
}
