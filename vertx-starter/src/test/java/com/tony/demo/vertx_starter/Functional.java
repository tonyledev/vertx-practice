package com.tony.demo.vertx_starter;

import io.vertx.core.Future;
import io.vertx.core.impl.future.CompositeFutureImpl;

import java.util.List;
import java.util.stream.Collectors;

public class Functional {
  private Functional() {
  }

  public static <R> Future<List<R>> allOfFutures(List<Future<R>> futures) {
    return CompositeFutureImpl.all(futures.toArray(new Future[futures.size()]))
      .map(v -> futures.stream()
        .map(Future::result)
        .collect(Collectors.toList())
      );
  }
}
