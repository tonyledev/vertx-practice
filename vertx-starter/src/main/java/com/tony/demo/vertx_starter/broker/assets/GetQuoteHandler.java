package com.tony.demo.vertx_starter.broker.assets;

import com.tony.demo.vertx_starter.broker.quotes.Quote;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Optional;

public class GetQuoteHandler implements Handler<RoutingContext> {
  private static final Logger LOGGER = LoggerFactory.getLogger(GetQuoteHandler.class);
  final Map<String, Quote> cachedQuotes;

  public GetQuoteHandler(Map<String, Quote> cachedQuotes) {
    this.cachedQuotes = cachedQuotes;
  }

  @Override
  public void handle(RoutingContext context) {
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
  }
}
