package de.local.energycharts.api.v1.solarcity.statistic;

import de.local.energycharts.solarcity.model.Time;
import org.junit.jupiter.api.Test;

import static de.local.energycharts.api.v1.solarcity.statistic.YearsFilter.createFilter;
import static de.local.energycharts.solarcity.model.AnnualSolarInstallations.Addition;
import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;

class YearsFilterTest {

  @Test
  void only_previous_solar_installations() {
    Time.freezeNowAt(2024);
    var addition2003 = Addition.builder().year(2003).build();
    var addition2004 = Addition.builder().year(2004).build();
    var addition2024 = Addition.builder().year(2024).build();
    var addition2025 = Addition.builder().year(2025).build();

    var filter = createFilter(true, 20,
        asList(
            addition2003,
            addition2004,
            addition2024,
            addition2025
        )
    );

    assertThat(filter.filter(addition2003)).isFalse();
    assertThat(filter.filter(addition2004)).isTrue();
    assertThat(filter.filter(addition2024)).isTrue();
    assertThat(filter.filter(addition2025)).isFalse();
  }

  @Test
  void also_future_solar_installations() {
    Time.freezeNowAt(2024);
    var addition2003 = Addition.builder().year(2003).build();
    var addition2014 = Addition.builder().year(2014).build();
    var addition2015 = Addition.builder().year(2015).build();
    var addition2024 = Addition.builder().year(2024).build();
    var addition2035 = Addition.builder().year(2035).build();

    var filter = createFilter(false, 20,
        asList(
            addition2003,
            addition2014,
            addition2015,
            addition2024,
            addition2035
        )
    );

    assertThat(filter.filter(addition2003)).isFalse();
    assertThat(filter.filter(addition2014)).isFalse();
    assertThat(filter.filter(addition2015)).isTrue();
    assertThat(filter.filter(addition2024)).isTrue();
    assertThat(filter.filter(addition2035)).isTrue();
  }
}