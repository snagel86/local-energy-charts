package de.local.energycharts.solarcity.gateway;

import reactor.core.publisher.Flux;

/**
 * @see <a href="https://public.opendatasoft.com/explore/dataset/georef-germany-postleitzahl/information/">Opendatasoft: Postleitzahlen - Germany</a>
 */
public interface OpendatasoftGateway {

    Flux<Integer> getPostcodes(String cityName);
}
