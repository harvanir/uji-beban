package org.harvanir.ujibeban.gateway.configuration;

import org.harvanir.ujibeban.core.UjiBebanProperties;
import org.harvanir.ujibeban.gateway.DefaultGateway;
import org.harvanir.ujibeban.gateway.Gateway;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

/** @author Harvan Irsyadi */
@Configuration(proxyBeanMethods = false)
public class GatewayConfiguration {

  @Bean
  public WebClient defaultWebClient(UjiBebanProperties properties) {
    return WebClient.builder().baseUrl(properties.getTargetHost()).build();
  }

  @Bean
  public Gateway gateway(WebClient defaultWebClient) {
    return new DefaultGateway(defaultWebClient);
  }
}
