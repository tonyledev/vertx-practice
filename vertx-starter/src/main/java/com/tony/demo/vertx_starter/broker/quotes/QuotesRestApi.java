package com.tony.demo.vertx_starter.broker.quotes;

import com.tony.demo.vertx_starter.broker.Assets;
import com.tony.demo.vertx_starter.broker.AssetsRestApi;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

public class QuotesRestApi {
  private static final Logger LOGGER = LoggerFactory.getLogger(QuotesRestApi.class);

  public static void attach(Router parent) {
    final Map<String, Quote> cachedQuotes = new HashMap<>();
    AssetsRestApi.ASSETS.forEach(symbol ->
      cachedQuotes.put(symbol, initRandomQuote(symbol))
    );

    parent.get("/quotes/:assets").handler(context -> {
      final String assetParam = context.pathParam("assets");
      LOGGER.debug("Asset parameter: {}", assetParam);

//      var quote = initRandomQuote(assetParam);
      var maybeQuote = Optional.ofNullable(cachedQuotes.get(assetParam)); // using it to receive object quote from cache
      if (maybeQuote.isEmpty()) {
        context.response()
          .setStatusCode(HttpResponseStatus.NOT_FOUND.code())
          .end(new JsonObject()
            .put("message", "quote for asset " + assetParam + " not available!")
            .put("path", context.normalizedPath())
            .toBuffer()
          );
        return;
      }
      final JsonObject response = maybeQuote.get().toJsonObject();
      LOGGER.info("Path {} response with {}", context.normalizedPath(), response.encode());
      context.response().end(response.toBuffer());
    });
  }

  private static Quote initRandomQuote(String assetParam) {
    return Quote.builder()
      .assets(new Assets(assetParam))
      .ask(randomValue())
      .bid(randomValue())
      .lastPrice(randomValue())
      .volume(randomValue())
      .build();
  }

  private static BigDecimal randomValue() {
    return BigDecimal.valueOf(ThreadLocalRandom.current().nextDouble(1, 100));
  }
}
