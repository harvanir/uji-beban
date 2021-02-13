package org.harvanir.ujibeban.core;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/** @author Harvan Irsyadi */
@Getter
@Setter
@ConfigurationProperties(prefix = "uji-beban")
public class UjiBebanProperties {

  private String targetHost;
}
