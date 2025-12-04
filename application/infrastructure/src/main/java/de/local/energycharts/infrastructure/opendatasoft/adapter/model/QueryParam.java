package de.local.energycharts.infrastructure.opendatasoft.adapter.model;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class QueryParam {

  private final String cityName;

  public static QueryParam createBy(String cityName) {
    return new QueryParam(cityName);
  }

  /**
   * Depending on whether the city is a state city or not, this method selects
   * the appropriate query param name, 'refine.lan_name' for a city-state or
   * 'refine.plz_name' for a city.
   *
   * @return 'refine.plz_name' if city or 'refine.lan_name' if city-state.
   * @see <a href="https://public.opendatasoft.com/explore/dataset/georef-germany-postleitzahl/api/">opendatasoft API: Postleitzahlen - Germany</a>.
   */
  public String name() {
    if (isStadtstaat()) {
      return "refine.lan_name";
    }
    return "refine.plz_name";
  }

  private boolean isStadtstaat() {
    return cityName.equals("Berlin") || cityName.equals("Bremen") || cityName.equals("Hamburg");
  }
}
