package de.local.energycharts.api.v1.mastr.model.mapper;

import de.local.energycharts.core.model.SolarCity;
import de.local.energycharts.api.v1.mastr.model.SolarCityCreatedResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface SolarCityCreatedMapper {

  @Mapping(target = "cityName", source = "name")
  @Mapping(target = "totalNumberOfSolarInstallations", source = "solarCity", qualifiedByName = "calculateTotalNumberOfSolarInstallations")
  SolarCityCreatedResponse mapToResponse(SolarCity solarCity);

  @Named("calculateTotalNumberOfSolarInstallations")
  default Integer locationToLocationDto(SolarCity solarCity) {
    return solarCity.calculateTotalNumberOfSolarInstallations();
  }
}
