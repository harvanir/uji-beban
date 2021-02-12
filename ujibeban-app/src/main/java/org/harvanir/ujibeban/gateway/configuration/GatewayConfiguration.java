package org.harvanir.ujibeban.gateway.configuration;

import org.harvanir.ujibeban.gateway.DefaultGateway;
import org.harvanir.ujibeban.gateway.Gateway;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

/** @author Harvan Irsyadi */
@Configuration(proxyBeanMethods = false)
public class GatewayConfiguration {

  @Bean
  public WebClient defaultWebClient() {
    return WebClient.builder().baseUrl("http://localhost:8080/").build();
  }

  @Bean
  public Gateway gateway(WebClient defaultWebClient) {
    return new DefaultGateway(defaultWebClient);
  }
}
