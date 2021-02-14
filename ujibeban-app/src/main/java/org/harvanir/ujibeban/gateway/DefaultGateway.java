package org.harvanir.ujibeban.gateway;

import org.harvanir.ujibeban.core.Request;
import org.harvanir.ujibeban.core.RolloverIterator;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

/** @author Harvan Irsyadi */
public class DefaultGateway implements Gateway {

  private final WebClient defaultWebClient;

  private final RolloverIterator iterator;

  public DefaultGateway(WebClient defaultWebClient, RolloverIterator iterator) {
    this.defaultWebClient = defaultWebClient;
    this.iterator = iterator;
  }

  @Override
  public Mono<String> call() {
    return Mono.defer(
        () -> {
          Request request = iterator.next();

          return defaultWebClient
              .method(request.getMethod())
              .uri(request.getPath())
              .retrieve()
              .bodyToMono(String.class);
        });
  }
}
