package de.local.energycharts.solarcity.writer;

import de.local.energycharts.solarcity.model.SolarCity;

import java.io.File;

public interface SolarSystemsXlsWriter {

    File write(SolarCity solarCity);
}
