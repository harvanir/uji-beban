package org.harvanir.ujibeban.core;

import java.util.Map;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.http.HttpMethod;

/** @author Harvan Irsyadi */
@Getter
@Setter
@ConfigurationProperties(prefix = "uji-beban")
public class UjiBebanProperties {

  private Map<String, Request> requests;

  @EqualsAndHashCode
  @Getter
  @Setter
  public static class Request {

    private HttpMethod method;

    private String path;
  }
}
