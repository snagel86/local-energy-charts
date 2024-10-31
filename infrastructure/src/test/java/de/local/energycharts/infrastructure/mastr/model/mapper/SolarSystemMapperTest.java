package de.local.energycharts.infrastructure.mastr.model.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import de.local.energycharts.infrastructure.adapter.mastr.model.mapper.SolarSystemMapper;
import de.local.energycharts.solarcity.model.SolarSystem;
import de.local.energycharts.solarcity.model.SolarSystem.Status;
import de.local.energycharts.infrastructure.adapter.mastr.model.EinheitJson;

import java.time.Instant;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

class SolarSystemMapperTest {

  private final SolarSystemMapper solarSystemMapper = Mappers.getMapper(SolarSystemMapper.class);

  @Test
  void test_mapping_from_solar_einheit() {
    EinheitJson mastrSolarEinheit = new EinheitJson();
    mastrSolarEinheit.setId("1234");
    mastrSolarEinheit.setInbetriebnahmeDatum("/Date(1628467200000)/");
    mastrSolarEinheit.setDatumLetzteAktualisierung("/Date(1629107527571)/");
    mastrSolarEinheit.setPlz("60314");
    mastrSolarEinheit.setBruttoleistung(10.4);
    mastrSolarEinheit.setNettonennleistung(7.28);
    mastrSolarEinheit.setBetriebsStatusName("In Betrieb");
    mastrSolarEinheit.setAnlagenbetreiberName("Mainova AG");
    mastrSolarEinheit.setEinheitName("PV-Anlage");

    SolarSystem solarSystem = SolarSystem.builder()
        .id("1234")
        .commissioning(LocalDate.of(2021, 8, 9))
        .lastChange(Instant.parse("2021-08-16T09:52:07.571Z"))
        .postcode(60314)
        .installedGrossPowerkWp(10.4)
        .installedNetPowerkWp(7.28)
        .status(Status.IN_OPERATION)
        .operatorName("Mainova AG")
        .name("PV-Anlage")
        .build();

    assertThat(solarSystem).isEqualTo(solarSystemMapper.map(mastrSolarEinheit));
  }

  @Test
  void test_convert_rest_api_date() {
    assertThat(solarSystemMapper.convertDate("/Date(1628467200000)/"))
        .isEqualTo(LocalDate.of(2021, 8, 9));

    assertThat(solarSystemMapper.convertDateTime("/Date(1629107527571)/"))
        .isEqualTo(Instant.parse("2021-08-16T09:52:07.571Z"));
  }
}
