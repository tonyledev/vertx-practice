package com.tony.demo.vertx_starter;

import com.tony.demo.vertx_starter.broker.MainVerticle;
import io.vertx.core.Vertx;
import io.vertx.ext.web.client.WebClient;
import io.vertx.ext.web.client.WebClientOptions;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(VertxExtension.class)
public class TestQuoteRestApi {
  private static final Logger LOGGER = LoggerFactory.getLogger(TestQuoteRestApi.class);

  @BeforeEach
  void deploy_verticle(Vertx vertx, VertxTestContext testContext) {
    vertx.deployVerticle(new MainVerticle(), testContext.succeeding(id -> testContext.completeNow()));
  }

  @Test
  void run_quote_for_assets(Vertx vertx, VertxTestContext context) throws Throwable {
    var client = WebClient.create(vertx, new WebClientOptions().setDefaultPort(com.tony.demo.vertx_starter.broker.MainVerticle.PORT));
    client.get("/quotes/AAAA")
      .send()
      .onComplete(context.succeeding(response -> {
        var json = response.bodyAsJsonObject();
        LOGGER.info("Response: {}", json);
        assertEquals("{\"name\":\"AAAA\"}", json.getJsonObject("assets").encode());
        assertEquals(200, response.statusCode());
        context.completeNow();
      }));
  }

  @Test
  void run_not_found_for_unknown_assets(Vertx vertx, VertxTestContext context) throws Throwable {
    var client = WebClient.create(vertx, new WebClientOptions().setDefaultPort(com.tony.demo.vertx_starter.broker.MainVerticle.PORT));
    client.get("/quotes/UNKNOWN")
      .send()
      .onComplete(context.succeeding(response -> {
        var json = response.bodyAsJsonObject();
        LOGGER.info("Response: {}", json);
        assertEquals("{\"message\":\"quote for asset UNKNOWN not available!\",\"path\":\"/quotes/UNKNOWN\"}", json.encode());
        assertEquals(404, response.statusCode());
        context.completeNow();
      }));
  }
}
