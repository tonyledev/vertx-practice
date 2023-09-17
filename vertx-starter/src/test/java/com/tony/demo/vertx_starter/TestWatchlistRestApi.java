package com.tony.demo.vertx_starter;

import com.tony.demo.vertx_starter.broker.Assets;
import com.tony.demo.vertx_starter.broker.MainVerticle;
import com.tony.demo.vertx_starter.broker.watchlist.WatchList;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.WebClient;
import io.vertx.ext.web.client.WebClientOptions;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(VertxExtension.class)
public class TestWatchlistRestApi extends AbstractRestApiTest {
  private static final Logger LOGGER = LoggerFactory.getLogger(TestWatchlistRestApi.class);

  @Test
  void adds_and_returns_watchlist_for_account(Vertx vertx, VertxTestContext context) throws Throwable {
    var client = WebClient.create(vertx, new WebClientOptions().setDefaultPort(TEST_SERVER_PORT));
    var accountId = UUID.randomUUID();
    client.put("/account/watchlist/" + accountId)
      .sendJsonObject(getBody())
      .onComplete(context.succeeding(response -> {
        var json = response.bodyAsJsonObject();
        LOGGER.info("Response PUT: {}", json);
        assertEquals("{\"assets\":[{\"name\":\"AAAA\"},{\"name\":\"BBBB\"}]}", json.encode());
        assertEquals(200, response.statusCode());
      }))
      .compose(next -> {
        client.get("/account/watchlist/" + accountId)
          .send()
          .onComplete(context.succeeding(response -> {
            var json = response.bodyAsJsonObject();
            LOGGER.info("Response GET: {}", json);
            assertEquals("{\"assets\":[{\"name\":\"AAAA\"},{\"name\":\"BBBB\"}]}", json.encode());
            assertEquals(200, response.statusCode());
            context.completeNow();
          }));
        return Future.succeededFuture();
      });
  }

  @Test
  void adds_and_deletes_watchlist_for_account(Vertx vertx, VertxTestContext context) {
    var client = WebClient.create(vertx, new WebClientOptions().setDefaultPort(TEST_SERVER_PORT));
    var accountId = UUID.randomUUID();
    client.put("/account/watchlist/" + accountId)
      .sendJsonObject(getBody())
      .onComplete(context.succeeding(response -> {
        var json = response.bodyAsJsonObject();
        LOGGER.info("Response PUT: {}", json);
        assertEquals("{\"assets\":[{\"name\":\"AAAA\"},{\"name\":\"BBBB\"}]}", json.encode());
        assertEquals(200, response.statusCode());
      }))
      .compose(next -> {
        client.delete("/account/watchlist/" + accountId)
          .send()
          .onComplete(context.succeeding(response -> {
            var json = response.bodyAsJsonObject();
            LOGGER.info("Response DELETE: {}", json);
            assertEquals("{\"assets\":[{\"name\":\"AAAA\"},{\"name\":\"BBBB\"}]}", json.encode());
            assertEquals(200, response.statusCode());
            context.completeNow();
          }));
        return Future.succeededFuture();
      });
  }

  private static JsonObject getBody() {
    return new WatchList(Arrays.asList(new Assets("AAAA"), new Assets("BBBB"))).toJsonObject();
  }
}
