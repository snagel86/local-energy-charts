package de.local.energycharts.api.v1.solarcity.admin.model.mapper;

import de.local.energycharts.api.v1.solarcity.admin.model.CreatedResponse;
import de.local.energycharts.solarcity.model.SolarCity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface SolarCityCreatedMapper {

  @Mapping(target = "cityName", source = "name")
  @Mapping(target = "totalNumberOfSolarInstallations", source = "solarCity", qualifiedByName = "getTotalNumberOfSolarInstallations")
  CreatedResponse mapToResponse(SolarCity solarCity);

  @Named("getTotalNumberOfSolarInstallations")
  default Integer getTotalNumberOfSolarInstallations(SolarCity solarCity) {
    return solarCity.getAllSolarSystems().size();
  }
}
