package de.local.energycharts.web.api.solarcity.statistic.model.highcharts.mapper;

import de.local.energycharts.core.solarcity.model.AnnualSolarInstallations;
import de.local.energycharts.core.solarcity.model.MonthlySolarInstallations;
import de.local.energycharts.web.api.solarcity.statistic.model.highcharts.Column;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.time.format.TextStyle;
import java.util.Locale;

@Mapper(componentModel = "spring")
public interface ColumnMapper {

  @Mapping(target = "id", source = "addition.year")
  @Mapping(target = "name", source = "addition", qualifiedByName = "getName")
  @Mapping(target = "year", source = "addition.year")
  @Mapping(target = "y", source = "totalInstalledMWp")
  @Mapping(target = "numberOfSolarSystems", source = "numberOfSolarSystems")
  @Mapping(target = "numberOfSolarSystemsUpTo1kWp", source = "numberOfSolarSystemsUpTo1kWp")
  @Mapping(target = "numberOfSolarSystems1To10kWp", source = "numberOfSolarSystems1To10kWp")
  @Mapping(target = "numberOfSolarSystems10To40kWp", source = "numberOfSolarSystems10To40kWp")
  @Mapping(target = "numberOfSolarSystemsFrom40kWp", source = "numberOfSolarSystemsFrom40kWp")
  Column mapToColumn(AnnualSolarInstallations.Addition addition);

  @Named("getName")
  default String getName(AnnualSolarInstallations.Addition addition) {
    return addition.getYear().toString();
  }

  @Mapping(target = "id", source = "addition", qualifiedByName = "getId")
  @Mapping(target = "name", source = "addition", qualifiedByName = "getName")
  @Mapping(target = "year", source = "addition.year")
  @Mapping(target = "y", source = "totalInstalledMWp")
  @Mapping(target = "numberOfSolarSystems", source = "numberOfSolarSystems")
  @Mapping(target = "numberOfSolarSystemsUpTo1kWp", source = "numberOfSolarSystemsUpTo1kWp")
  @Mapping(target = "numberOfSolarSystems1To10kWp", source = "numberOfSolarSystems1To10kWp")
  @Mapping(target = "numberOfSolarSystems10To40kWp", source = "numberOfSolarSystems10To40kWp")
  @Mapping(target = "numberOfSolarSystemsFrom40kWp", source = "numberOfSolarSystemsFrom40kWp")
  Column mapToColumn(MonthlySolarInstallations.Addition addition);

  @Named("getId")
  default String getId(MonthlySolarInstallations.Addition addition) {
    return addition.getMonth() + " " + addition.getYear();
  }

  @Named("getName")
  default String getName(MonthlySolarInstallations.Addition addition) {
    return addition.getMonth().getDisplayName(TextStyle.SHORT, Locale.GERMAN);
  }
}
