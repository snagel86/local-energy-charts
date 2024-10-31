package de.local.energycharts.statistic.ports.out;

import de.local.energycharts.solarcity.model.SolarCity;

import java.io.File;

public interface SolarSystemsXlsWriter {

    File write(SolarCity solarCity);
}
