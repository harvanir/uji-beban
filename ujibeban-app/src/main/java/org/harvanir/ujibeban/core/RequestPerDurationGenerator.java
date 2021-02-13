package org.harvanir.ujibeban.core;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.ParallelFlux;
import reactor.core.publisher.SynchronousSink;

/** @author Harvan Irsyadi */
@Slf4j
public class RequestPerDurationGenerator {

  private static final int SKIP = -1;

  private RequestPerDurationGenerator() {}

  private static boolean isTimeout(LocalDateTime start, int duration) {
    return ChronoUnit.SECONDS.between(start, LocalDateTime.now()) >= duration;
  }

  public static <T> ParallelFlux<T> of(int virtualUser, int duration, Mono<T> callback) {
    AtomicInteger loopCounter = new AtomicInteger(0);

    return Flux.generate(getSink(virtualUser, duration, loopCounter))
        .parallel(virtualUser)
        .flatMap(emit -> dispatchCallback(emit, callback, loopCounter));
  }

  private static Consumer<SynchronousSink<Integer>> getSink(
      int virtualUser, int duration, AtomicInteger loopCounter) {
    LocalDateTime start = LocalDateTime.now();

    return (SynchronousSink<Integer> sink) -> {
      if (isTimeout(start, duration)) {
        sink.complete();
      } else {
        int current = loopCounter.get();
        log(current);

        if (current < virtualUser) {
          sink.next(loopCounter.incrementAndGet());
        } else {
          sink.next(SKIP);
        }
      }
    };
  }

  private static <T> Mono<T> dispatchCallback(
      Integer emit, Mono<T> callback, AtomicInteger loopCounter) {
    if (!emit.equals(SKIP)) {
      return callback
          .doOnError(e -> log.error("an error occur", e))
          .doOnCancel(() -> log.warn("canceled"))
          .doFinally(signalType -> loopCounter.decrementAndGet());
    }

    return Mono.empty();
  }

  private static void log(int value) {
    if (value < 0) {
      log.warn("invalid number: {}", value);
    }
  }
}
