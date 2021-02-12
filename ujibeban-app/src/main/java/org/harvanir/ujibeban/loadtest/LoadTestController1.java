package org.harvanir.ujibeban.loadtest;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import lombok.extern.slf4j.Slf4j;
import org.harvanir.ujibeban.gateway.Gateway;
import org.reactivestreams.Publisher;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.SynchronousSink;

/** @author Harvan Irsyadi */
@Slf4j
// @RestController
public class LoadTestController1 {

  private static final int SKIP = -1;

  private final Gateway gateway;

  public LoadTestController1(Gateway gateway) {
    this.gateway = gateway;
  }

  @GetMapping(value = "/load-test", produces = MediaType.TEXT_PLAIN_VALUE)
  public Mono<byte[]> loadTest(
      @RequestParam(name = "virtualUser") Integer virtualUser,
      @RequestParam(name = "duration") Integer duration) {
    return load(virtualUser, duration);
  }

  private Mono<byte[]> load(Integer virtualUser, Integer duration) {
    LocalDateTime start = LocalDateTime.now();
    AtomicInteger loopCounter = new AtomicInteger(0);
    AtomicInteger counter = new AtomicInteger(0);

    return Flux.generate(
            (SynchronousSink<Integer> sink) -> {
              boolean timeOut = ChronoUnit.SECONDS.between(start, LocalDateTime.now()) >= duration;

              if (timeOut) {
                sink.complete();
              } else {
                int current = loopCounter.get();
                log(current);

                if (current < virtualUser) {
                  counter.incrementAndGet();

                  current = loopCounter.incrementAndGet();
                  sink.next(current);
                } else {
                  sink.next(SKIP);
                }
              }
            })
        .flatMap(dispatch(loopCounter))
        .then(constructResponse(counter));
  }

  private Function<Integer, Publisher<String>> dispatch(AtomicInteger loopCounter) {
    return data -> {
      if (data.equals(SKIP)) {
        return Mono.empty();
      }

      return gateway.call().doOnNext(s -> loopCounter.getAndDecrement());
    };
  }

  private void log(int value) {
    if (value < 0) {
      log.warn("invalid number: {}", value);
    }
  }

  private Mono<byte[]> constructResponse(AtomicInteger counter) {
    return Mono.fromSupplier(() -> String.format("Total request: %s", counter.get()).getBytes());
  }
}
