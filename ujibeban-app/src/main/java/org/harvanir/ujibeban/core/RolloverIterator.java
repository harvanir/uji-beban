package org.harvanir.ujibeban.core;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;

/** @author Harvan Irsyadi */
public class RolloverIterator {

  @Deprecated private final ConcurrentLinkedQueue<Request> queue;

  private final AtomicInteger counter;

  private final Request[] requests;

  private final int max;

  private static final Object lock = new Object();

  private Integer cnt = 0;

  public RolloverIterator(List<Request> requests) {
    this.requests = requests.toArray(new Request[0]);
    queue = new ConcurrentLinkedQueue<>();
    max = requests.size();
    counter = new AtomicInteger(0);
    populate(requests);
  }

  private void populate(Collection<Request> requests) {
    queue.addAll(requests);
  }

  public Request next() {
    if (requests.length == 1) {
      return requests[0];
    }

    synchronized (lock) {
      if (cnt < requests.length) {
        return requests[cnt++];
      }
      cnt = 0;
      return requests[cnt++];
    }
  }

  /** Can't handled in parallelism. */
  public Request nextOrigin() {
    int index = counter.getAndIncrement();
    if (index < max) {
      return requests[index];
    }
    counter.updateAndGet(operand -> 0);
    index = counter.getAndIncrement();

    return requests[index];
  }

  /** @deprecated Still observing for performance. */
  @Deprecated
  public Request next2() {
    if (requests.length == 1) {
      return queue.peek();
    }

    Request pool = queue.poll();
    queue.offer(pool);

    return pool;
  }
}
