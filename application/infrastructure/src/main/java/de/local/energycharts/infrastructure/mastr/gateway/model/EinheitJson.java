package de.local.energycharts.infrastructure.mastr.gateway.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class EinheitJson {

  @JsonProperty("Id")
  private String id;
  @JsonProperty("EnergietraegerName")
  private String energietraegerName;
  @JsonProperty("BetriebsStatusName")
  private String betriebsStatusName;
  @JsonProperty("InbetriebnahmeDatum")
  private String inbetriebnahmeDatum;
  @JsonProperty("DatumLetzteAktualisierung")
  private String datumLetzteAktualisierung;
  @JsonProperty("Ort")
  private String ort;
  @JsonProperty("Plz")
  private String plz;
  @JsonProperty("Bruttoleistung")
  private Double bruttoleistung;
  @JsonProperty("Nettonennleistung")
  private Double nettonennleistung;
  @JsonProperty("AnlagenbetreiberName")
  private String anlagenbetreiberName;
  @JsonProperty("EinheitName")
  private String einheitName;
}
