package com.tony.demo.vertx_starter;

import com.tony.demo.vertx_starter.broker.ConfigLoader;
import com.tony.demo.vertx_starter.broker.MainVerticle;
import io.netty.handler.codec.http.HttpHeaderValues;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpHeaders;
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
public class TestAssetsRestApi extends AbstractRestApiTest {
  private static final Logger LOGGER = LoggerFactory.getLogger(TestAssetsRestApi.class);

  @Test
  void run_all_assets(Vertx vertx, VertxTestContext context) throws Throwable {
    var client = WebClient.create(vertx, new WebClientOptions().setDefaultPort(TEST_SERVER_PORT));
    client.get("/assets")
      .send()
      .onComplete(context.succeeding(response -> {
        var json = response.bodyAsJsonArray();
        LOGGER.info("Response: {}", json);
        assertEquals("[{\"name\":\"AAAA\"},{\"name\":\"BBBB\"},{\"name\":\"CCCC\"},{\"name\":\"DDDD\"},{\"name\":\"EEEE\"}]", json.encode());
        assertEquals(200, response.statusCode());
        assertEquals(HttpHeaderValues.APPLICATION_JSON.toString(), response.getHeader(HttpHeaders.CONTENT_TYPE.toString()));
        assertEquals("my-value", response.getHeader("my-header"));
        context.completeNow();
      }));
  }
}
