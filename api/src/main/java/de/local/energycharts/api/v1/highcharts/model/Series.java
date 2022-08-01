package de.local.energycharts.api.v1.highcharts.model;

import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Series {

  private String id;
  private String name;
  private List<AdditionOfSolarInstallationsResponse> data;
}
