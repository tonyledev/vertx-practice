package com.tony.demo.vertx_starter.broker.assets;

import com.tony.demo.vertx_starter.broker.Assets;
import com.tony.demo.vertx_starter.broker.AssetsRestApi;
import com.tony.demo.vertx_starter.broker.quotes.Quote;
import io.vertx.ext.web.Router;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

public class QuotesRestApi {

  public static void attach(Router parent) {
    final Map<String, Quote> cachedQuotes = new HashMap<>();
    AssetsRestApi.ASSETS.forEach(symbol ->
      cachedQuotes.put(symbol, initRandomQuote(symbol))
    );

    parent.get("/quotes/:assets").handler(new GetQuoteHandler(cachedQuotes));
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
