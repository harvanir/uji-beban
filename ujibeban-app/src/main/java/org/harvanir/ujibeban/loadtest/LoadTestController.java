package org.harvanir.ujibeban.loadtest;

import java.util.concurrent.atomic.AtomicInteger;
import lombok.extern.slf4j.Slf4j;
import org.harvanir.ujibeban.core.RequestPerDurationGenerator;
import org.harvanir.ujibeban.gateway.Gateway;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

/** @author Harvan Irsyadi */
@Slf4j
@RestController
public class LoadTestController {

  private final Gateway gateway;

  public LoadTestController(Gateway gateway) {
    this.gateway = gateway;
  }

  @GetMapping(value = "/load-test", produces = MediaType.TEXT_PLAIN_VALUE)
  public Mono<byte[]> loadTest(
      @RequestParam(name = "virtualUser") Integer virtualUser,
      @RequestParam(name = "duration") Integer duration) {
    AtomicInteger counter = new AtomicInteger(0);
    Mono<String> callback =
        gateway
            .call()
            .doOnNext(v -> counter.incrementAndGet())
            .doOnCancel(() -> log.error("canceled"))
            .doOnError(
                e -> {
                  log.error("error", e);
                  counter.incrementAndGet();
                });
    return RequestPerDurationGenerator.of(virtualUser, duration, callback)
        .then()
        .then(constructResponse(counter));
  }

  private Mono<byte[]> constructResponse(AtomicInteger counter) {
    return Mono.fromSupplier(
        () -> {
          log.info("construct response");
          return String.format("Total request: %s", counter.get()).getBytes();
        });
  }
}
