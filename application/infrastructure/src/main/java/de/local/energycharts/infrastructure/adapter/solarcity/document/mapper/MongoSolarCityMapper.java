package de.local.energycharts.infrastructure.adapter.solarcity.document.mapper;

import de.local.energycharts.core.solarcity.model.SolarCity;
import de.local.energycharts.infrastructure.adapter.solarcity.document.MongoSolarCity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface MongoSolarCityMapper {

  MongoSolarCity mapToMongoDocument(SolarCity solarCity);

  @Mapping(target = "allSolarSystems", ignore = true)
  @Mapping(target = "allPostcodes", ignore = true)
  SolarCity mapToDomainModel(MongoSolarCity mongoSolarCity);

  List<SolarCity> mapToDomainModels(List<MongoSolarCity> mongoSolarCities);
}
