package com.tony.demo.vertx_starter.broker;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class VersionInfoVerticle extends AbstractVerticle {
  private static final Logger LOGGER = LoggerFactory.getLogger(VersionInfoVerticle.class);
  @Override
  public void start(Promise<Void> startPromise) throws Exception {
    ConfigLoader.load(vertx)
      .onFailure(startPromise::fail)
      .onSuccess(configuration -> {
        LOGGER.info("Current application version is: {}", configuration.getVersion());
        startPromise.complete();
      });
  }
}
