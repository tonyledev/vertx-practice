package com.tony.demo.vertx_starter.eventbus;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.DeliveryOptions;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RequestResponseExample {
  public static void main(String[] args) {
    var vert = Vertx.vertx();
    vert.deployVerticle(new RequestVerticle());
    vert.deployVerticle(new ResponseVerticle());
  }

  static class RequestVerticle extends AbstractVerticle {
    private static final Logger LOGGER = LoggerFactory.getLogger(RequestVerticle.class);
    public static final String ADDRESS = "my.request.address";

    @Override
    public void start(Promise<Void> startPromise) throws Exception {
      startPromise.complete();
      var eventBus = vertx.eventBus();
      final var message = new JsonObject()
        .put("message", "Hello World")
        .put("version", 1);
      LOGGER.debug("Sending {}", message);
      eventBus.request(ADDRESS, message, reply -> {
        LOGGER.debug("Response : {}", reply.result().body());
      });
    }
  }

  static class ResponseVerticle extends AbstractVerticle {
    private static final Logger LOGGER = LoggerFactory.getLogger(ResponseVerticle.class);
    @Override
    public void start(Promise<Void> startPromise) throws Exception {
      startPromise.complete();
      vertx.eventBus().consumer(RequestVerticle.ADDRESS, message -> {
        LOGGER.debug("Received message: {}", message.body());
        message.reply(new JsonArray().add("one").add("two").add("three"));
      });
    }
  }
}
