package com.tony.demo.vertx_starter.verticles;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class VerticleA extends AbstractVerticle {
  private static final Logger LOGGER = LoggerFactory.getLogger(VerticleA.class);

  @Override
  public void start(Promise<Void> startPromise) throws Exception {
    LOGGER.debug("Start " + getClass().getName());
    vertx.deployVerticle(new VerticleAA(), whenDeployed -> {
      System.out.println("Deployed " + VerticleAA.class.getName());
      vertx.undeploy(whenDeployed.result());
    });
    vertx.deployVerticle(new VerticleAB(), whenDeployed -> {
      System.out.println("Deployed " + VerticleAB.class.getName());
      // Do not undeploy
    });
    startPromise.complete();
  }
}
