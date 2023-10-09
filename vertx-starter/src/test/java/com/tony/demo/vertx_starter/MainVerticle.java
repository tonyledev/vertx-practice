package com.tony.demo.vertx_starter;


import io.vertx.core.*;
import io.vertx.core.impl.future.CompositeFutureImpl;

public class MainVerticle extends AbstractVerticle {

  public static void main(String[] args) {
    Vertx vertx = Vertx.vertx();

    vertx.deployVerticle(new MainVerticle());
  }

    @Override
    public void start() {
      Promise<String> promise1 = Promise.promise();
      vertx.setTimer(500, id -> {
        promise1.complete("Success");
      });

      Promise<Integer> promise2 = Promise.promise();

      Future<String> future1 = promise1.future();
      Future<Integer> future2 = promise2.future();

      // Simulate asynchronous operations
      vertx.setTimer(1000, timerId -> {
        future1.onSuccess(result -> {
          Future.succeededFuture("Hello");
        });
      });

      vertx.setTimer(2000, timerId -> {
        future2.onSuccess(result -> {
          Future.succeededFuture(42);
        });
      });

      CompositeFutureImpl.all(future1, future2).onComplete(result -> {
        if (result.succeeded()) {
          String message = future1.result() + future2.result();
          System.out.println(message);
        } else {
          System.err.println("One or more futures failed.");
        }
      });
    }
}
