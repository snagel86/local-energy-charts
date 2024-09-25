package de.local.energycharts.solarcity.usecase;

import reactor.core.publisher.Mono;

import java.io.File;

public interface CreateAllSolarSystemsXls {

  Mono<File> createAllSolarSystemsXls(String id);
}
