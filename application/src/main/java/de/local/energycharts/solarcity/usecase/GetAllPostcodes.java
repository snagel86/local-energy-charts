package de.local.energycharts.solarcity.usecase;

import reactor.core.publisher.Flux;

public interface GetAllPostcodes {

  Flux<Integer> getAllPostcodes(String id);
}
