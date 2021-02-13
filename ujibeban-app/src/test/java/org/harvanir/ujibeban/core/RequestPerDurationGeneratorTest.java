package org.harvanir.ujibeban.core;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.Duration;
import java.util.concurrent.atomic.AtomicInteger;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;

/** @author Harvan Irsyadi */
class RequestPerDurationGeneratorTest {

  @Test
  void givenTenVirtualUser_whenExecute_thenInvokedTenTimes() {
    int virtualUser = 10;
    AtomicInteger counter = new AtomicInteger(0);
    Mono<Integer> callback =
        Mono.fromSupplier(counter::incrementAndGet).delayElement(Duration.ofSeconds(1));
    RequestPerDurationGenerator.of(virtualUser, 1, callback).then().block();

    assertEquals(10, counter.get());
  }
}
