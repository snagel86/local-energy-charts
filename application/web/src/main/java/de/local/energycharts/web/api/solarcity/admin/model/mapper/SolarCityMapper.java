package de.local.energycharts.web.api.solarcity.admin.model.mapper;

import de.local.energycharts.core.solarcity.model.SolarCity;
import de.local.energycharts.web.api.solarcity.admin.model.SolarCityResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SolarCityMapper {

  SolarCityResponse mapToResponse(SolarCity solarCity);
}
