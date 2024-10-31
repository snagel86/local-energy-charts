package de.local.energycharts.statistic.model.mapper;

import de.local.energycharts.solarcity.model.SolarCity;
import de.local.energycharts.statistic.model.SolarCityStatistic;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SolarCityStatisticMapper {

  SolarCityStatistic map(SolarCity solarCity);
}
