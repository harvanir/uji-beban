package org.harvanir.ujibeban.core;

import java.util.stream.Collectors;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/** @author Harvan Irsyadi */
@EnableConfigurationProperties(UjiBebanProperties.class)
@Configuration(proxyBeanMethods = false)
public class UjiBebanConfiguration {

  @Bean
  public RolloverIterator rolloverIterator(UjiBebanProperties properties) {
    return new RolloverIterator(
        properties.getRequests().entrySet().stream()
            .map(
                entry ->
                    Request.builder()
                        .name(entry.getKey())
                        .method(entry.getValue().getMethod())
                        .path(entry.getValue().getPath())
                        .build())
            .collect(Collectors.toList()));
  }
}
