package org.harvanir.ujibeban.core;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;

/** @author Harvan Irsyadi */
class RequestPerSecondGeneratorTest {

  @Test
  void givenTenVirtualUser_whenExecute_thenInvokedTenTimes() {
    int virtualUser = 10;
    AtomicInteger counter = new AtomicInteger(0);
    Mono<Integer> callback =
        Mono.fromSupplier(counter::incrementAndGet).delayElement(Duration.ofSeconds(1));
    RequestPerSecondGenerator.of(virtualUser, 1, callback).then().block();

    assertEquals(10, counter.get());
  }
}
