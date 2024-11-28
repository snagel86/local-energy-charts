package de.local.energycharts.solarcity.ports.in;

import org.jmolecules.architecture.hexagonal.PrimaryPort;
import reactor.core.publisher.Mono;

import java.io.File;

@PrimaryPort
public interface CreateSolarXls {

  Mono<File> allSolarSystems(String id);
}
