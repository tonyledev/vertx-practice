package com.tony.demo.vertx_starter.eventloops;

import io.vertx.core.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

public class EventLoopExample extends AbstractVerticle {
  private static final Logger LOGGER = LoggerFactory.getLogger(EventLoopExample.class);
  public static void main(String[] args) {
    var vertx = Vertx.vertx(new VertxOptions()
      .setMaxEventLoopExecuteTime(500)
      .setMaxWorkerExecuteTimeUnit(TimeUnit.MILLISECONDS)
      .setBlockedThreadCheckInterval(1)
      .setBlockedThreadCheckIntervalUnit(TimeUnit.SECONDS)
      .setEventLoopPoolSize(2)); // nho hon so instance
    vertx.deployVerticle(EventLoopExample.class.getName(), new DeploymentOptions()
      .setInstances(4));
  }
  @Override
  public void start(Promise<Void> startPromise) throws Exception {
    LOGGER.debug("Start " + getClass().getName());
    startPromise.complete();
    //Do not do this inside a verticle
//    Thread.sleep(5000);
  }
}
