package de.local.energycharts.infrastructure.solarcity.model.mapper;

import de.local.energycharts.infrastructure.solarcity.model.MongoSolarCity;
import de.local.energycharts.solarcity.model.SolarCity;
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
