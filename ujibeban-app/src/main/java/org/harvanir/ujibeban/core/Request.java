package org.harvanir.ujibeban.core;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.http.HttpMethod;

/** @author Harvan Irsyadi */
@ToString
@Builder
@EqualsAndHashCode
@Getter
@Setter
public class Request {

  private String name;

  private HttpMethod method;

  private String path;
}
