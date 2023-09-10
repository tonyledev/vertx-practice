package com.tony.demo.vertx_starter.worker;

import com.tony.demo.vertx_starter.eventloops.EventLoopExample;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WorkerExample extends AbstractVerticle {
  private static final Logger LOGGER = LoggerFactory.getLogger(WorkerExample.class);
  public static void main(String[] args) {
    var vertx = Vertx.vertx();
    vertx.deployVerticle(new WorkerExample());
  }

  @Override
  public void start(Promise<Void> startPromise) throws Exception {
    vertx.deployVerticle(new WorkerVerticle(),
      new DeploymentOptions()
        .setWorker(true)
        .setWorkerPoolSize(1)
        .setWorkerPoolName("my-worker-verticle"));
    startPromise.complete();
    executeBlockingCode();
  }

  private void executeBlockingCode() {
    vertx.executeBlocking(event -> {
      LOGGER.debug("Executing blocking code");
      try {
        Thread.sleep(5000);
        event.complete();  // success call blocking
//        event.fail("Force fail");
      } catch (InterruptedException e) {
        LOGGER.error("Failed: " + e);
        event.fail(e);
      }
    }, result -> {
      if (result.succeeded()) {
        LOGGER.debug("Blocking call done");
      } else {
        LOGGER.debug("Blocking call failed due to: " , result.cause());
      }
    });
  }
}
