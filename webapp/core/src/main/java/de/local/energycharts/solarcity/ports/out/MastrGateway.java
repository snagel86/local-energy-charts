package de.local.energycharts.solarcity.ports.out;

import de.local.energycharts.solarcity.model.SolarSystem;
import org.jmolecules.architecture.hexagonal.SecondaryPort;
import reactor.core.publisher.Flux;

import java.time.LocalDate;

/**
 * @see <a href="https://www.marktstammdatenregister.de/MaStR">MaStR</a>
 */
@SecondaryPort
public interface MastrGateway {

    Flux<SolarSystem> getSolarSystemsByPostcode(Integer postcode);

    Flux<SolarSystem> getSolarSystemsByMunicipalityKey(String municipalityKey, LocalDate lastUpdate);
}
