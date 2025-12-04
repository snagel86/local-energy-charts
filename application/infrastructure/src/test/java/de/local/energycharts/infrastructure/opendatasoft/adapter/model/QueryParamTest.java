package de.local.energycharts.infrastructure.opendatasoft.adapter.model;

import org.junit.jupiter.api.Test;

import static de.local.energycharts.infrastructure.opendatasoft.adapter.model.QueryParam.createBy;
import static org.assertj.core.api.Assertions.assertThat;

class QueryParamTest {

  @Test
  void create_query_param_depending_if_it_is_a_city_state_or_not() {
    assertThat(createBy("Berlin").name()).isEqualTo("refine.lan_name");
    assertThat(createBy("Bremen").name()).isEqualTo("refine.lan_name");
    assertThat(createBy("Hamburg").name()).isEqualTo("refine.lan_name");
    assertThat(createBy("Frankfurt am Main").name()).isEqualTo("refine.plz_name");
  }
}