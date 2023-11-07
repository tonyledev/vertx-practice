package com.tony.demo.vertx_starter;

import io.vertx.core.*;

import java.util.List;

public class MyVerticle extends AbstractVerticle {
  public static void main(String[] args) {
    Vertx vertx = Vertx.vertx();

    vertx.deployVerticle(new MyVerticle());
  }
  @Override
  public void start(Promise<Void> startPromise) throws Exception {
    // Perform multiple asynchronous tasks
    Future<String> asyncTask1 = performAsyncTask("Task 1").onComplete(result1 -> {
      System.out.println("Task 1 succeeded with result: " + result1.result());
    });
    Future<String> asyncTask2 = performAsyncTask2("Task 2").onComplete(result2 -> {
      System.out.println("Task 2 succeeded with result: " + result2.result());
    });
    Future<String> asyncTask3 = performAsyncTask3("Task 3").onComplete(result3 -> {
      System.out.println("Task 3 succeeded with result: " + result3.result());
    });

    CompositeFuture com = Future.all(asyncTask1, asyncTask2, asyncTask3);
    com.onComplete(result -> {
      if (result.succeeded()) {
        String result1 = com.resultAt(0);
        String result2 = com.resultAt(1);
        String result3 = com.resultAt(2);
        System.out.println("All tasks succeeded with results: " + result1);
        System.out.println("All tasks succeeded with results: " + result2);
        System.out.println("All tasks succeeded with results: " + result3);
      } else {
        Throwable exception = result.cause();
        System.err.println("At least one task failed with exception: " + exception.getMessage());
      }
    });
  }

  private Future<String> performAsyncTask(String taskName) {
    Promise<String> asyncFuture = Promise.promise();

    vertx.setTimer(1000, timerId -> {
      for (int i = 0; i < 10; i++) {
        System.out.println("hello " + i);
      }
      asyncFuture.complete(taskName + " completed");
    });

    return asyncFuture.future();
  }

  private Future<String> performAsyncTask2(String taskName) {
    Promise<String> asyncFuture = Promise.promise();

    vertx.setTimer(5000, timerId -> {
      for (int i = 0; i < 5; i++) {
        System.out.println("hello " + i);
      }
      asyncFuture.complete(taskName + " completed");
    });

    return asyncFuture.future();
  }

  private Future<String> performAsyncTask3(String taskName) {
    Promise<String> asyncFuture = Promise.promise();

    vertx.setTimer(15000, timerId -> {
      for (int i = 0; i < 15; i++) {
        System.out.println("hello " + i);
      }
      asyncFuture.complete(taskName + " completed");
    });

    return asyncFuture.future();
  }

  @Override
  public void stop(Promise<Void> stopPromise) throws Exception {
    super.stop(stopPromise);
  }
}
