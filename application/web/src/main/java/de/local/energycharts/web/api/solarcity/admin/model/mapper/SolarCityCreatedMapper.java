package de.local.energycharts.web.api.solarcity.admin.model.mapper;

import de.local.energycharts.core.solarcity.model.SolarCity;
import de.local.energycharts.web.api.solarcity.admin.model.CreatedResponse;
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
