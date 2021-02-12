package org.harvanir.ujibeban.loadtest;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.atomic.AtomicInteger;
import lombok.extern.slf4j.Slf4j;
import org.harvanir.ujibeban.gateway.Gateway;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.SynchronousSink;

/** @author Harvan Irsyadi */
@Slf4j
@RestController
public class LoadTestController {

  private static final int SKIP = -1;

  private final Gateway gateway;

  public LoadTestController(Gateway gateway) {
    this.gateway = gateway;
  }

  @GetMapping(value = "/load-test", produces = MediaType.TEXT_PLAIN_VALUE)
  public Mono<byte[]> loadTest(
      @RequestParam(name = "virtualUser") Integer virtualUser,
      @RequestParam(name = "duration") Integer duration) {
    AtomicInteger counter = new AtomicInteger(0);
    return RequestPerDurationGenerator.generate(virtualUser, duration)
        .doOnNext(integer -> counter.incrementAndGet())
        .flatMap(integer -> gateway.call())
        .then(constructResponse(counter));
  }

  private Mono<byte[]> constructResponse(AtomicInteger counter) {
    return Mono.fromSupplier(() -> String.format("Total request: %s", counter.get()).getBytes());
  }

  static class RequestPerDurationGenerator {

    private RequestPerDurationGenerator() {}

    public static Flux<Integer> generate(int virtualUser, int duration) {
      LocalDateTime start = LocalDateTime.now();
      AtomicInteger loopCounter = new AtomicInteger(0);

      return Flux.generate(
              (SynchronousSink<Integer> sink) -> {
                boolean timeOut =
                    ChronoUnit.SECONDS.between(start, LocalDateTime.now()) >= duration;

                if (timeOut) {
                  sink.complete();
                } else {
                  int current = loopCounter.get();
                  log(current);

                  if (current < virtualUser) {
                    current = loopCounter.incrementAndGet();
                    sink.next(current);
                  } else {
                    sink.next(SKIP);
                  }
                }
              })
          .flatMap(
              data -> {
                if (!data.equals(SKIP)) {
                  loopCounter.getAndDecrement();
                  return Flux.just(data);
                }

                return Flux.empty();
              });
    }

    private static void log(int value) {
      if (value < 0) {
        log.warn("invalid number: {}", value);
      }
    }
  }
}
