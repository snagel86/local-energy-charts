package de.local.energycharts.api.v1.highcharts.model;

public enum Color {
  DARK_SOLAR_BLUE("#012b4a"),
  KLIMAENTSCHEID_FFM_GREEN_YELLOW("#cde846");

  private String value;

  Color(String value) {
    this.value = value;
  }

  public String getValue() {
    return value;
  }
}
