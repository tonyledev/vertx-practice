package com.tony.demo.vertx_starter;

import com.tony.demo.vertx_starter.broker.ConfigLoader;
import com.tony.demo.vertx_starter.broker.MainVerticle;
import io.vertx.core.Vertx;
import io.vertx.junit5.VertxTestContext;
import org.junit.jupiter.api.BeforeEach;

public class AbstractRestApiTest {
  protected static final int TEST_SERVER_PORT = 9000;

  @BeforeEach
  void deploy_verticle(Vertx vertx, VertxTestContext testContext) {
    System.setProperty(ConfigLoader.SERVER_PORT, String.valueOf(TEST_SERVER_PORT));
    vertx.deployVerticle(new MainVerticle(), testContext.succeeding(id -> testContext.completeNow()));
  }
}
