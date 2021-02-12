// package org.harvanir.ujibeban.loadtest;
//
// import lombok.extern.slf4j.Slf4j;
// import org.harvanir.ujibeban.gateway.Gateway;
// import org.reactivestreams.Publisher;
// import org.springframework.http.MediaType;
// import org.springframework.web.bind.annotation.GetMapping;
// import org.springframework.web.bind.annotation.RequestParam;
// import org.springframework.web.bind.annotation.RestController;
// import reactor.core.publisher.Flux;
// import reactor.core.publisher.Mono;
// import reactor.core.publisher.SynchronousSink;
//
// import java.time.LocalDateTime;
// import java.time.temporal.ChronoUnit;
// import java.util.concurrent.atomic.AtomicInteger;
// import java.util.function.Consumer;
// import java.util.function.Function;
//
/// ** @author Harvan Irsyadi */
// @Slf4j
// @RestController
// public class LoadTestController2 {
//
//  private static final int SKIP = -1;
//
//  private final Gateway gateway;
//
//  public LoadTestController2(Gateway gateway) {
//    this.gateway = gateway;
//  }
//
//  @GetMapping(value = "/load-test", produces = MediaType.TEXT_PLAIN_VALUE)
//  public Mono<byte[]> loadTest(
//      @RequestParam(name = "virtualUser") Integer virtualUser,
//      @RequestParam(name = "duration") Integer duration) {
//    AtomicInteger counter = new AtomicInteger(0);
//    return load(virtualUser, duration, counter);
//  }
//
//  private Mono<byte[]> load(Integer virtualUser, Integer duration, AtomicInteger counter) {
//    RequestPerDurationSink sink =
//        new RequestPerDurationSink(virtualUser, LocalDateTime.now(), duration, counter);
//
//    return Flux.generate(sink)
//        .flatMap(sink.callback(gateway.call()))
//        .then(constructResponse(counter));
//  }
//
//  static class RequestPerDurationSink implements Consumer<SynchronousSink<Integer>> {
//
//    private final int virtualUser;
//
//    private final LocalDateTime start;
//
//    private final int duration;
//
//    private final AtomicInteger loopCounter;
//
//    private final AtomicInteger counter;
//
//    public RequestPerDurationSink(int virtualUser, LocalDateTime start, int duration,
// AtomicInteger counter) {
//      this.virtualUser = virtualUser;
//      this.start = start;
//      this.duration = duration;
//      this.loopCounter = new AtomicInteger(0);
//      this.counter = counter;
//    }
//
//    @Override
//    public void accept(SynchronousSink<Integer> sink) {
//      boolean timeOut = ChronoUnit.SECONDS.between(start, LocalDateTime.now()) >= duration;
//
//      if (!timeOut) {
//        int current = loopCounter.get();
//
//        if (current < virtualUser) {
//          counter.incrementAndGet();
//
//          sink.next(loopCounter.incrementAndGet());
//        } else {
//          sink.next(SKIP);
//        }
//
//        log(current);
//      } else {
//        sink.complete();
//      }
//    }
//
//    public <T> Function<Integer, Publisher<T>> callback(Mono<T> callback) {
//      return data -> {
//        if (!data.equals(SKIP)) {
//          return callback.doFinally(signalType -> loopCounter.decrementAndGet());
//        }
//
//        return Mono.empty();
//      };
//    }
//
//    private void log(int value) {
//      if (value < 0) {
//        log.warn("invalid number: {}", value);
//      }
//    }
//  }
//
//  private Mono<byte[]> constructResponse(AtomicInteger counter) {
//    return Mono.fromSupplier(() -> String.format("Total request: %s", counter.get()).getBytes());
//  }
// }
