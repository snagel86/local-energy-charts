package de.local.energycharts.web.api.solarcity.statistic.model.highcharts.mapper;

import de.local.energycharts.core.solarcity.model.MonthlySolarInstallations;
import de.local.energycharts.web.api.solarcity.statistic.model.highcharts.Spline;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface SplineMapper {

  @Mapping(target = "y", source = "numberOfSolarSystems")
  Spline mapToSpline(MonthlySolarInstallations.Addition addition);
}
