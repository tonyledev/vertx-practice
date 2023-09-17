package com.tony.demo.vertx_starter.broker;

import io.netty.handler.codec.http.HttpHeaderValues;
import io.vertx.core.http.HttpHeaders;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;

public class AssetsRestApi {
  private static final Logger LOGGER = LoggerFactory.getLogger(AssetsRestApi.class);
  public static final List<String> ASSETS = Arrays.asList("AAAA", "BBBB", "CCCC", "DDDD", "EEEE");
  static void attach(Router parent) {
    parent.get("/assets").handler(context -> {
      final JsonArray response = new JsonArray();
      ASSETS.stream().map(Assets::new).forEach(response::add);
//      response
//        .add(new Assets("AAAA"))
//        .add(new Assets("BBBB"))
//        .add(new Assets("CCCC"))
//        .add(new Assets("DDDD"));
      LOGGER.info("Path {} response with {}", context.normalizedPath(), response.encode());
      context.response()
        .putHeader(HttpHeaders.CONTENT_TYPE, HttpHeaderValues.APPLICATION_JSON)
        .putHeader("my-header", "my-value")
        .end(response.toBuffer());
    });
  }
}
