package de.local.energycharts.solarcity.model;

import lombok.Data;
import lombok.experimental.Accessors;
import org.jmolecules.ddd.annotation.Entity;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Accessors(chain = true)
public class SolarBuildingPieChart {

  private Double totalInstalledMWp = 0.0;
  private List<Slice> slices = new ArrayList<>(0);

  public void addSlice(Slice slice) {
    if (slices == null) {
      slices = new ArrayList<>();
    }
    slices.add(slice);
  }

  public Slice getSlice(SliceType type) {
    return slices.stream()
        .filter(slice -> slice.getType().equals(type))
        .findAny()
        .orElse(null);
  }

  @Data
  @Accessors(chain = true)
  public static class Slice {
    private SliceType type;
    private Double installedMWp;
    private Integer count;
    private Double percentage;
  }

  public enum SliceType {
    BALKONKRAFTWERKE, HOMES, APARTMENT_BUILDINGS_COMMERCIAL_AND_CO, SCHOOLS_INDUSTRIAL_BUILDINGS_AND_CO;
  }
}
