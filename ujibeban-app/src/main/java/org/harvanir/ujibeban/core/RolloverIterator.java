package org.harvanir.ujibeban.core;

import java.util.Collection;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;

/** @author Harvan Irsyadi */
public class RolloverIterator {

  @Deprecated private final Queue<Request> queue;

  private final AtomicInteger counter;

  private final Request[] requests;

  private final int max;

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
    Request pool = queue.poll();
    queue.offer(pool);

    return pool;
  }
}
