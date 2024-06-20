package de.local.energycharts.infrastructure.mastr.model;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class DataTest {

  @Test
  void test_page_size() {
    Data data = new Data();
    data.setPage(1)
        .setTotal(6100);
    do {
      data.setPage(data.getNextPage());
    } while (data.getNextPage() != null);

    assertThat(data.getPage()).isEqualTo(7);
  }
}