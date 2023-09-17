package com.tony.demo.vertx_starter.broker.quotes;

import com.tony.demo.vertx_starter.broker.Assets;
import io.vertx.core.json.JsonObject;
import lombok.Builder;
import lombok.Value;

import java.math.BigDecimal;

@Value
@Builder
public class Quote {
  Assets assets;
  BigDecimal bid;
  BigDecimal ask;
  BigDecimal lastPrice;
  BigDecimal volume;

  public JsonObject toJsonObject() {
    return JsonObject.mapFrom(this);
  }
}
