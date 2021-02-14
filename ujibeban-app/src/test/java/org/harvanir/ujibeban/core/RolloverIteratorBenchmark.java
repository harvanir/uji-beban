package org.harvanir.ujibeban.core;

import java.util.ArrayList;
import java.util.List;
import org.harvanir.ujibeban.benchmark.BaseBenchmark;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.OptionsBuilder;

/** @author Harvan Irsyadi */
@State(Scope.Benchmark)
public class RolloverIteratorBenchmark extends BaseBenchmark {

  private final RolloverIterator iterator = createIterator();

  public static void main(String[] args) throws RunnerException {
    OptionsBuilder builder = new OptionsBuilder();
    builder.include("RolloverIteratorBenchmark");
    new RolloverIteratorBenchmark().run(builder);
  }

  private RolloverIterator createIterator() {
    List<Request> requests = new ArrayList<>();
    requests.add(createRequest("request1"));
    requests.add(createRequest("request2"));
    requests.add(createRequest("request3"));

    return new RolloverIterator(requests);
  }

  private Request createRequest(String name) {
    return Request.builder().name(name).build();
  }

  @Benchmark
  @BenchmarkMode(Mode.Throughput)
  public void next() {
    iterator.next();
  }

  @Benchmark
  @BenchmarkMode(Mode.Throughput)
  public void next2() {
    iterator.next2();
  }
}
