package de.local.energycharts.api.v1.solarcity.create.model.mapper;

import de.local.energycharts.api.v1.solarcity.create.model.SolarCityResponse;
import de.local.energycharts.solarcity.model.SolarCity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SolarCityMapper {

  SolarCityResponse mapToResponse(SolarCity solarCity);
}
