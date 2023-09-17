package com.tony.demo.vertx_starter.broker.assets;

import com.tony.demo.vertx_starter.broker.Assets;
import io.netty.handler.codec.http.HttpHeaderValues;
import io.vertx.core.Handler;
import io.vertx.core.http.HttpHeaders;
import io.vertx.core.json.JsonArray;
import io.vertx.ext.web.RoutingContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GetAssetsHandler implements Handler<RoutingContext> {
  private static final Logger LOGGER = LoggerFactory.getLogger(GetAssetsHandler.class);
  @Override
  public void handle(RoutingContext context) {
      final JsonArray response = new JsonArray();
      AssetsRestApi.ASSETS.stream().map(Assets::new).forEach(response::add);
      LOGGER.info("Path {} response with {}", context.normalizedPath(), response.encode());
      context.response()
        .putHeader(HttpHeaders.CONTENT_TYPE, HttpHeaderValues.APPLICATION_JSON)
        .putHeader("my-header", "my-value")
        .end(response.toBuffer());
  }
}
