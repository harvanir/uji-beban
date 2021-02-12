package org.harvanir.ujibeban.gateway;

import reactor.core.publisher.Mono;

/** @author Harvan Irsyadi */
public interface Gateway {

  Mono<String> call();
}
