package org.harvanir.ujibeban.core;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;

/** @author Harvan Irsyadi */
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
}
