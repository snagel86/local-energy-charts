package de.local.energycharts.api.v1.solarcity.create.model.mapper;

import de.local.energycharts.solarcity.model.SolarCity;
import de.local.energycharts.api.v1.solarcity.create.model.SolarCityCreatedResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface SolarCityCreatedMapper {

  @Mapping(target = "cityName", source = "name")
  @Mapping(target = "totalNumberOfSolarInstallations", source = "solarCity", qualifiedByName = "calculateTotalNumberOfSolarInstallations")
  SolarCityCreatedResponse mapToResponse(SolarCity solarCity);

  @Named("calculateTotalNumberOfSolarInstallations")
  default Integer calculateTotalNumberOfSolarInstallations(SolarCity solarCity) {
    return solarCity.calculateTotalNumberOfSolarInstallations();
  }
}
