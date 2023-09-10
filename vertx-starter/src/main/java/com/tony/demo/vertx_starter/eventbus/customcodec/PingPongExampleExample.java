package com.tony.demo.vertx_starter.eventbus.customcodec;

import io.vertx.core.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PingPongExampleExample {
  private static final Logger LOGGER = LoggerFactory.getLogger(PingPongExampleExample.class);
  public static void main(String[] args) {
    var vert = Vertx.vertx();
    vert.deployVerticle(new PingVerticle(), logOnError());
    vert.deployVerticle(new PongVerticle(), logOnError());
  }

  private static Handler<AsyncResult<String>> logOnError() {
    return ar -> {
      if (ar.failed()) {
        LOGGER.error("err", ar.cause());
      }
    };
  }

  public static class PingVerticle extends AbstractVerticle {
    private static final Logger LOGGER = LoggerFactory.getLogger(PingVerticle.class);
    public static final String ADDRESS = PingVerticle.class.getName();

    @Override
    public void start(Promise<Void> startPromise) throws Exception {
      var eventBus = vertx.eventBus();
      final Ping message = new Ping("Hello", true);
      LOGGER.debug("Sending {}", message);

      // Register only once
      eventBus.registerDefaultCodec(Ping.class, new LocalMessageCodec<>(Ping.class));
      eventBus.<Pong>request(ADDRESS, message, reply -> {
        if (reply.failed()) {
          LOGGER.error("Failed: ", reply.cause());
          return;
        }
        LOGGER.debug("Response : {}", reply.result().body());
      });
      startPromise.complete();
    }
  }

  public static class PongVerticle extends AbstractVerticle {
    private static final Logger LOGGER = LoggerFactory.getLogger(PongVerticle.class);
    @Override
    public void start(Promise<Void> startPromise) throws Exception {
      // Register only once
      vertx.eventBus().registerDefaultCodec(Pong.class, new LocalMessageCodec<>(Pong.class));
      vertx.eventBus().<Ping>consumer(PingVerticle.ADDRESS, message -> {
        LOGGER.debug("Received message: {}", message.body());
        message.reply(new Pong(0));
      }).exceptionHandler(error -> {
        LOGGER.error("Error : ", error);
      });

      startPromise.complete();
    }
  }
}
