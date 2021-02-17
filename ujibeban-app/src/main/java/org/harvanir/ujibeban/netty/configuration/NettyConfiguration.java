package org.harvanir.ujibeban.netty.configuration;

import static reactor.netty.resources.ConnectionProvider.DEFAULT_POOL_MAX_CONNECTIONS;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorResourceFactory;
import reactor.netty.http.HttpResources;
import reactor.netty.resources.ConnectionProvider;

/** @author Harvan Irsyadi */
@Configuration(proxyBeanMethods = false)
public class NettyConfiguration {

  @Bean
  public ReactorResourceFactory reactorServerResourceFactory() {
    ReactorResourceFactory resourceFactory = new ReactorResourceFactory();
    resourceFactory.addGlobalResourcesConsumer(
        httpResources -> HttpResources.set(getHttpConnectionProvider("http")));

    return resourceFactory;
  }

  private ConnectionProvider getHttpConnectionProvider(String name) {
    return ConnectionProvider.builder(name)
        .pendingAcquireMaxCount(DEFAULT_POOL_MAX_CONNECTIONS * 2)
        .build();
  }
}
