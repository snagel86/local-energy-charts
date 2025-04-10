package de.local.energycharts.solarcity.ports.out;

import org.jmolecules.architecture.hexagonal.SecondaryPort;
import reactor.core.publisher.Flux;

/**
 * @see <a href="https://public.opendatasoft.com/explore/dataset/georef-germany-postleitzahl/information/">Opendatasoft: Postleitzahlen - Germany</a>
 */
@SecondaryPort
public interface OpendatasoftGateway {

    Flux<Integer> getPostcodes(String cityName);
}
