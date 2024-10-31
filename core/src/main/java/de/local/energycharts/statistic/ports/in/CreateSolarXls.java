package de.local.energycharts.statistic.ports.in;

import reactor.core.publisher.Mono;

import java.io.File;

public interface CreateSolarXls {

  Mono<File> allSolarSystems(String id);
}
