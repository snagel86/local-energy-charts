package de.local.energycharts.api.v1.highcharts.model.mapper;

import static de.local.energycharts.api.v1.highcharts.model.Color.DARK_SOLAR_BLUE;
import static de.local.energycharts.api.v1.highcharts.model.Color.KLIMAENTSCHEID_FFM_GREEN_YELLOW;

import de.local.energycharts.api.v1.highcharts.model.AdditionOfSolarInstallationsResponse;
import de.local.energycharts.core.model.statistic.AdditionOfSolarInstallations;
import java.time.format.TextStyle;
import java.util.Locale;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface HighchartsAdditionOfSolarInstallationsMapper {

  @Mapping(target = "id", source = "addition.year")
  @Mapping(target = "name", source = "addition", qualifiedByName = "getName")
  @Mapping(target = "y", source = "totalInstalledMWp")
  @Mapping(target = "drilldown", source = "addition.year")
  @Mapping(target = "numberOfSolarSystems", source = "numberOfSolarSystems")
  @Mapping(target = "numberOfSolarSystemsUpTo10kWp", source = "numberOfSolarSystemsUpTo10kWp")
  @Mapping(target = "numberOfSolarSystems10To40kWp", source = "numberOfSolarSystems10To40kWp")
  @Mapping(target = "numberOfSolarSystemsFrom40kWp", source = "numberOfSolarSystemsFrom40kWp")
  @Mapping(target = "color", source = "addition", qualifiedByName = "getColor")
  AdditionOfSolarInstallationsResponse mapToResponse(AdditionOfSolarInstallations addition);

  @Named("getName")
  default String getName(AdditionOfSolarInstallations addition) {
    if (addition.getMonth() == null) {
      return addition.getYear().toString();
    }
    return addition.getMonth().getDisplayName(TextStyle.FULL_STANDALONE, new Locale("de", "DE"));
  }

  @Named("getColor")
  default String getColor(AdditionOfSolarInstallations addition) {
    return addition.isCalculatedInFuture() ? KLIMAENTSCHEID_FFM_GREEN_YELLOW.getValue() : DARK_SOLAR_BLUE.getValue();
  }
}
