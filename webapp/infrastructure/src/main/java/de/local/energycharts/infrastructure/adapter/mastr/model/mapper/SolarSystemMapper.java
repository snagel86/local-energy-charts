package de.local.energycharts.infrastructure.adapter.mastr.model.mapper;

import de.local.energycharts.solarcity.model.SolarSystem;
import de.local.energycharts.solarcity.model.SolarSystem.Status;
import de.local.energycharts.infrastructure.adapter.mastr.model.EinheitJson;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface SolarSystemMapper {

  // e. g. /Date(1628467200000)/
  Pattern restApiDatePattern = Pattern.compile("^\\/Date\\((\\d+)\\)\\/$");

  @Mapping(target = "id", source = "id")
  @Mapping(target = "commissioning", source = "inbetriebnahmeDatum", qualifiedByName = "convertDate")
  @Mapping(target = "installedGrossPowerkWp", source = "bruttoleistung")
  @Mapping(target = "installedNetPowerkWp", source = "nettonennleistung")
  @Mapping(target = "lastChange", source = "datumLetzteAktualisierung", qualifiedByName = "convertDateTime")
  @Mapping(target = "status", source = "betriebsStatusName", qualifiedByName = "convertStatus")
  @Mapping(target = "name", source = "einheitName")
  @Mapping(target = "postcode", source = "plz")
  @Mapping(target = "operatorName", source = "anlagenbetreiberName")
  SolarSystem map(EinheitJson einheitJson);

  @Named("convertDate")
  default LocalDate convertDate(String restApiDate) {
    if (restApiDate != null) {
      Matcher matcher = restApiDatePattern.matcher(restApiDate);
      if (matcher.find()) {
        long epochMillis = Long.parseLong(matcher.group(1));
        return Instant.ofEpochMilli(epochMillis).atZone(ZoneId.of("UTC")).toLocalDate();
      }
    }
    return LocalDate.of(1970, 1, 1);
  }

  @Named("convertDateTime")
  default Instant convertDateTime(String restApiDateTime) {
    if (restApiDateTime != null) {
      Matcher matcher = restApiDatePattern.matcher(restApiDateTime);
      if (matcher.find()) {
        long epochMillis = Long.parseLong(matcher.group(1));
        return Instant.ofEpochMilli(epochMillis);
      }
    }
    return Instant.ofEpochMilli(0L);
  }

  @Named("convertStatus")
  default Status convertStatus(String status) {
    if (status != null && status.equalsIgnoreCase("In Betrieb")) {
      return Status.IN_OPERATION;
    }
    if (status != null && status.equalsIgnoreCase("In Planung")) {
      return Status.IN_PLANNING;
    }
    if (status != null && status.equalsIgnoreCase("Endgültig stillgelegt")) {
      return Status.PERMANENTLY_SHUT_DOWN;
    }
    if (status != null && status.equalsIgnoreCase("Vorübergehend stillgelegt")) {
      return Status.TEMPORARILY_SHUT_DOWN;
    }
    return Status.NONE;
  }
}
