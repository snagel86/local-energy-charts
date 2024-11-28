package de.local.energycharts.solarcity.ports.out;

import de.local.energycharts.solarcity.model.SolarCity;
import org.jmolecules.architecture.hexagonal.SecondaryPort;

import java.io.File;

@SecondaryPort
public interface SolarSystemsXlsWriter {

    File write(SolarCity solarCity);
}
