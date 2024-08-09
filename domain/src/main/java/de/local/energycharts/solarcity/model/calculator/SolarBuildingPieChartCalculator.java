package de.local.energycharts.solarcity.model.calculator;

import de.local.energycharts.solarcity.model.SolarSystem;
import de.local.energycharts.solarcity.model.statistic.SolarBuildingPieChart;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Set;

import static de.local.energycharts.solarcity.model.statistic.SolarBuildingPieChart.SliceType.*;

@RequiredArgsConstructor
public class SolarBuildingPieChartCalculator {

  private final Set<SolarSystem> solarSystems;

  private SolarBuildingPieChart pieChart;

  public SolarBuildingPieChart calculatePieChart() {
    pieChart = new SolarBuildingPieChart();

    if (!solarSystems.isEmpty()) {
      pieChart.setTotalInstalledMWp(calculateTotalInstalledMWpOf(solarSystems));
      pieChart.addSlice(calculateSlice(SCHOOLS_INDUSTRIAL_BUILDINGS_AND_CO, 40.0, 9999999.9));
      pieChart.addSlice(calculateSlice(APARTMENT_BUILDINGS_COMMERCIAL_AND_CO, 10.0, 40.0));
      pieChart.addSlice(calculateSlice(HOMES, 1.0, 10.0));
      pieChart.addSlice(calculateSlice(BALKONKRAFTWERKE, 0.0, 1.0));
    }

    return pieChart;
  }

  private SolarBuildingPieChart.Slice calculateSlice(
      SolarBuildingPieChart.SliceType type,
      Double min_kWp,
      Double max_kWp
  ) {
    var solarSystemsOfSlice = solarSystems.stream()
        .filter(SolarSystem::isInOperation)
        .filter(solarSystem -> solarSystem.getInstalledNetPowerkWp() > min_kWp)
        .filter(solarSystem -> solarSystem.getInstalledNetPowerkWp() <= max_kWp)
        .toList();
    var installedMWp = calculateTotalInstalledMWpOf(solarSystemsOfSlice);

    return new SolarBuildingPieChart.Slice()
        .setType(type)
        .setInstalledMWp(installedMWp)
        .setCount(solarSystemsOfSlice.size())
        .setPercentage(installedMWp / pieChart.getTotalInstalledMWp() * 100.0);
  }

  private Double calculateTotalInstalledMWpOf(List<SolarSystem> solarSystems) {
    return solarSystems.stream().mapToDouble(SolarSystem::getInstalledNetPowerkWp).sum() / 1000.0; // kWp - > MWp
  }

  private Double calculateTotalInstalledMWpOf(Set<SolarSystem> solarSystems) {
    return solarSystems.stream().mapToDouble(SolarSystem::getInstalledNetPowerkWp).sum() / 1000.0; // kWp - > MWp
  }
}