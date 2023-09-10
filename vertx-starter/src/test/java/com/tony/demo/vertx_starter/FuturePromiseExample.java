package com.tony.demo.vertx_starter;

import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ExtendWith(VertxExtension.class)
public class FuturePromiseExample {
  private static final Logger LOGGER = LoggerFactory.getLogger(FuturePromiseExample.class);

  @Test
  void promise_success(Vertx vertx, VertxTestContext context) {
    final Promise<String> promise = Promise.promise();
    LOGGER.debug("Start");
    vertx.setTimer(500, id -> {
      promise.complete("Success");
      LOGGER.debug("Success");
      context.completeNow();
    });
    LOGGER.debug("End");
  }

  @Test
  void promise_failed(Vertx vertx, VertxTestContext context) {
    final Promise<String> promise = Promise.promise();

    vertx.setTimer(500, id -> {
      promise.fail(new RuntimeException("Failed!!!"));
      LOGGER.debug("Failed");
      context.completeNow();
    });
    LOGGER.debug("End");
  }

  @Test
  void future_success(Vertx vertx, VertxTestContext context) {
    final Promise<String> promise = Promise.promise();
    LOGGER.debug("Start");
    vertx.setTimer(500, id -> {
      promise.complete("Success");
      LOGGER.debug("Timer done");
      context.completeNow();
    });
    final Future<String> future = promise.future();
    future.onSuccess(result -> {
      LOGGER.debug("Result: {}", result);
      context.completeNow();
    }).onFailure(context::failNow);
  }

  @Test
  void future_failure(Vertx vertx, VertxTestContext context) {
    final Promise<String> promise = Promise.promise();
    LOGGER.debug("Start");
    vertx.setTimer(500, id -> {
      promise.fail(new RuntimeException("Failed"));
      LOGGER.debug("Timer done");
//      context.completeNow();
    });
    final Future<String> future = promise.future();
    future
      .onSuccess(context::failNow)
      .onFailure(error -> {
        LOGGER.debug("Result: ", error);
        context.completeNow();
    });
  }

  @Test
  void future_map(Vertx vertx, VertxTestContext context) {
    final Promise<String> promise = Promise.promise();
    LOGGER.debug("Start");
    vertx.setTimer(500, id -> {
      promise.complete("Success");
      LOGGER.debug("Timer done");
      context.completeNow();
    });
    final Future<String> future = promise.future();
    future
      .map(asString -> {
        LOGGER.debug("Map String to JsonObject");
        return new JsonObject().put("key", asString);
    })
      .map(jsonObject -> new JsonArray().add(jsonObject))
      .onSuccess(result -> {
      LOGGER.debug("Result: {} of type {}", result, result.getClass().getSimpleName());
      context.completeNow();
    }).onFailure(context::failNow);
  }

  @Test
  void future_coordination(Vertx vertx, VertxTestContext context) {
    vertx.createHttpServer()
      .requestHandler(httpServerRequest -> LOGGER.debug("{}", httpServerRequest))
      .listen(10_000)
      .compose(server -> {
        LOGGER.debug("Another task");
        return Future.succeededFuture(server);
      })
      .compose(server -> {
        LOGGER.debug("Even more");
        return Future.succeededFuture(server);
      })
      .onFailure(context::failNow)
      .onSuccess(httpServer -> {
        LOGGER.debug("Server started on port {}", httpServer.actualPort());
        context.completeNow();
      });
  }
}
