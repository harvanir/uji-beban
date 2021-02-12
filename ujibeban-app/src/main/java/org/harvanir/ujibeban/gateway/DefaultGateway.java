package org.harvanir.ujibeban.gateway;

import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

/** @author Harvan Irsyadi */
public class DefaultGateway implements Gateway {

  private final WebClient defaultWebClient;

  public DefaultGateway(WebClient defaultWebClient) {
    this.defaultWebClient = defaultWebClient;
  }

  @Override
  public Mono<String> call() {
    return defaultWebClient.get().uri("/base").retrieve().bodyToMono(String.class);
  }
}
