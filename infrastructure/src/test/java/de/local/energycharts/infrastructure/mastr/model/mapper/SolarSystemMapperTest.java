package de.local.energycharts.infrastructure.mastr.model.mapper;

import de.local.energycharts.infrastructure.adapter.mastr.model.EinheitJson;
import de.local.energycharts.infrastructure.adapter.mastr.model.mapper.SolarSystemMapper;
import de.local.energycharts.solarcity.model.SolarSystem;
import de.local.energycharts.solarcity.model.SolarSystem.Status;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.time.Instant;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

class SolarSystemMapperTest {

  private final SolarSystemMapper solarSystemMapper = Mappers.getMapper(SolarSystemMapper.class);

  @Test
  void test_mapping_from_solar_einheit() {
    var mastrSolarEinheit = new EinheitJson()
        .setId("1234")
        .setInbetriebnahmeDatum("/Date(1628467200000)/")
        .setDatumLetzteAktualisierung("/Date(1629107527571)/")
        .setPlz("60314")
        .setBruttoleistung(10.4)
        .setNettonennleistung(7.28)
        .setBetriebsStatusName("In Betrieb")
        .setAnlagenbetreiberName("Mainova AG")
        .setEinheitName("PV-Anlage");

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

  @Test
  void test_status_mapping() {
    var mastrSolarEinheit = new EinheitJson();

    mastrSolarEinheit.setBetriebsStatusName("In Betrieb");
    assertThat(solarSystemMapper.map(mastrSolarEinheit)
        .getStatus()
    ).isEqualTo(Status.IN_OPERATION);

    mastrSolarEinheit.setBetriebsStatusName("In Planung");
    assertThat(solarSystemMapper.map(mastrSolarEinheit)
        .getStatus()
    ).isEqualTo(Status.IN_PLANNING);

    mastrSolarEinheit.setBetriebsStatusName("Endgültig stillgelegt");
    assertThat(solarSystemMapper.map(mastrSolarEinheit)
        .getStatus()
    ).isEqualTo(Status.PERMANENTLY_SHUT_DOWN);

    mastrSolarEinheit.setBetriebsStatusName("Vorübergehend stillgelegt");
    assertThat(solarSystemMapper.map(mastrSolarEinheit)
        .getStatus()
    ).isEqualTo(Status.TEMPORARILY_SHUT_DOWN);
  }
}
