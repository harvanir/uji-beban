package org.harvanir.ujibeban.core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

/** @author Harvan Irsyadi */
@Slf4j
class RolloverIteratorTest {

  @Test
  void givenSingleSource_whenNext_thenReturnTheSame() {
    List<Request> requests = new ArrayList<>();
    Request request1 =
        Request.builder()
            .name("request1")
            .method(HttpMethod.GET)
            .path("http://localhost:8080/request1")
            .build();
    requests.add(request1);
    // assertion
    RolloverIterator rollover = new RolloverIterator(requests);
    assertEquals(request1, rollover.next());
    assertEquals(request1, rollover.next());
  }

  private Request getRequest(String name) {
    return Request.builder()
        .name(name)
        .method(HttpMethod.GET)
        .path("http://localhost:8080/" + name)
        .build();
  }

  @Test
  void givenMultipleSource_whenNext_thenReturnRollover() {
    List<Request> requests = new ArrayList<>();
    // request1
    Request request1 =
        Request.builder()
            .name("request1")
            .method(HttpMethod.GET)
            .path("http://localhost:8080/request1")
            .build();
    requests.add(request1);
    // request2
    Request request2 =
        Request.builder()
            .name("request2")
            .method(HttpMethod.GET)
            .path("http://localhost:8080/request2")
            .build();
    requests.add(request2);
    // assertion
    RolloverIterator rollover = new RolloverIterator(requests);
    assertEquals(request1, rollover.next());
    assertEquals(request2, rollover.next());
    assertEquals(request1, rollover.next());
    assertEquals(request2, rollover.next());
  }

  @Test
  void givenOneRequest_whenNextInConcurrency_thenSuccess() {
    RolloverIterator rollover =
        new RolloverIterator(Collections.singletonList(getRequest("request1")));
    Flux.range(1, 100000)
        .parallel()
        .runOn(Schedulers.parallel())
        .doOnNext(integer -> rollover.next())
        .then()
        .block();
    assertTrue(true);
  }

  @Test
  void givenTwoRequest_whenNextInConcurrency_thenSuccess() {
    RolloverIterator rollover =
        new RolloverIterator(Arrays.asList(getRequest("request1"), getRequest("request2")));
    Flux.range(1, 100000)
        .parallel()
        .runOn(Schedulers.parallel())
        .doOnNext(integer -> rollover.next())
        .then()
        .block();
    assertTrue(true);
  }

  @Test
  void givenThreeRequest_whenNextInConcurrency_thenSuccess() {
    RolloverIterator rollover =
        new RolloverIterator(
            Arrays.asList(getRequest("request1"), getRequest("request2"), getRequest("request1")));
    Flux.range(1, 100000)
        .parallel()
        .runOn(Schedulers.parallel())
        .doOnNext(integer -> rollover.next())
        .then()
        .block();
    assertTrue(true);
  }
}
