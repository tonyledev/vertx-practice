package com.tony.demo.vertx_starter.broker.watchlist;

import com.tony.demo.vertx_starter.broker.Assets;
import io.vertx.core.json.JsonObject;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WatchList {
  List<Assets> assets;

  public JsonObject toJsonObject() {
    return JsonObject.mapFrom(this);
  }
}
