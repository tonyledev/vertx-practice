package com.tony.demo.vertx_starter.broker.assets;

import io.vertx.ext.web.Router;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;

public class AssetsRestApi {
  public static final List<String> ASSETS = Arrays.asList("AAAA", "BBBB", "CCCC", "DDDD", "EEEE");

  static void attach(Router parent) {
    parent.get("/assets").handler(new GetAssetsHandler());
  }
}
