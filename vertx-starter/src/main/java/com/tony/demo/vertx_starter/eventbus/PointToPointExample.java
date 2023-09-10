package com.tony.demo.vertx_starter.eventbus;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/*
* With this approach, we can communicate in a thread safe way between verticles and don't have to worry about concurrency issue
* */
public class PointToPointExample {
  public static void main(String[] args) {
    var vertx = Vertx.vertx();
    vertx.deployVerticle(new Sender());
    vertx.deployVerticle(new Receiver());
  }

  static class Sender extends AbstractVerticle {
    private static final Logger LOGGER = LoggerFactory.getLogger(Sender.class);
    @Override
    public void start(Promise<Void> startPromise) throws Exception {
      startPromise.complete();
      // Sending a message every second
      vertx.setPeriodic(1000, id -> {
        vertx.eventBus().send(Sender.class.getName(), "Sending a message ...");
      });
      vertx.eventBus().send(Sender.class.getName(), "Sending a message ...");
    }
  }

  static class Receiver extends AbstractVerticle {
    private static final Logger LOGGER = LoggerFactory.getLogger(Receiver.class);
    @Override
    public void start(Promise<Void> startPromise) throws Exception {
      startPromise.complete();
      vertx.eventBus().consumer(Sender.class.getName(), message -> {
        LOGGER.debug("Received : {}", message.body());
      });
    }
  }
}
