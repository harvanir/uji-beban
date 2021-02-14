package org.harvanir.ujibeban.loadtest;

import java.util.Random;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

/** @author Harvan Irsyadi */
@Slf4j
@RestController
public class BaseController {

  private final Random random = new Random(5);

  @GetMapping(value = "/base", produces = MediaType.TEXT_PLAIN_VALUE)
  public Mono<byte[]> base() {
    return Mono.fromSupplier(() -> random.nextInt(9999)).map(v -> String.valueOf(v).getBytes());
  }

  @GetMapping(value = "/base2", produces = MediaType.TEXT_PLAIN_VALUE)
  public Mono<byte[]> base2() {
    return base();
  }
}
