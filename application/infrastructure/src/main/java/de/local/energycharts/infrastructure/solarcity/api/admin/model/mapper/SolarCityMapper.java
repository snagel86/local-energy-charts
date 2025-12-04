package de.local.energycharts.infrastructure.solarcity.api.admin.model.mapper;

import de.local.energycharts.infrastructure.solarcity.api.admin.model.SolarCityResponse;
import de.local.energycharts.core.solarcity.model.SolarCity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SolarCityMapper {

  SolarCityResponse mapToResponse(SolarCity solarCity);
}
