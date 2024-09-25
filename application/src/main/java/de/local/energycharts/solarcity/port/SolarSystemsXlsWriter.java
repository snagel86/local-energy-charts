package de.local.energycharts.solarcity.port;

import de.local.energycharts.solarcity.model.SolarCity;

import java.io.File;

public interface SolarSystemsXlsWriter {

    File write(SolarCity solarCity);
}
