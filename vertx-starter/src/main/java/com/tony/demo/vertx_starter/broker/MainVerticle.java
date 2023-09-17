package com.tony.demo.vertx_starter.broker;

import com.tony.demo.vertx_starter.broker.quotes.QuotesRestApi;
import com.tony.demo.vertx_starter.broker.watchlist.WatchlistRestApi;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Handler;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class  MainVerticle extends AbstractVerticle {
  private static final Logger LOGGER = LoggerFactory.getLogger(MainVerticle.class);
  public static void main(String[] args) {
    System.setProperty(ConfigLoader.SERVER_PORT, "9000");
    var vertx = Vertx.vertx();
    vertx.exceptionHandler(error ->
      LOGGER.error("Unhandled:", error)
    );
    vertx.deployVerticle(new MainVerticle(), ar -> {
      if (ar.failed()) {
        LOGGER.error("Failed to deploy:", ar.cause());
        return;
      }
      LOGGER.info("Deployed {}!", MainVerticle.class.getName());
    });
  }

  @Override
  public void start(Promise<Void> startPromise) throws Exception {
    ConfigLoader.load(vertx)
      .onFailure(startPromise::fail)
      .onSuccess(configuration -> {
        LOGGER.info("Retrieve Configuration: {}", configuration);
        startHttpServerAndAttachRoute(startPromise, configuration);
      });
  }

  private void startHttpServerAndAttachRoute(Promise<Void> startPromise, final BrokerConfig config) {
    final Router restApi = Router.router(vertx);
    restApi.route()
        .handler(BodyHandler.create())
        .failureHandler(handleFailure());
    AssetsRestApi.attach(restApi);
    QuotesRestApi.attach(restApi);
    WatchlistRestApi.attach(restApi);

    vertx.createHttpServer()
      .requestHandler(restApi)
      .exceptionHandler(error -> LOGGER.error("HTTP server error: ", error))
      .listen(config.getServerPort(), http -> {
        if (http.succeeded()) {
          startPromise.complete();
          LOGGER.info("HTTP server started on port {}, version {}", config.getServerPort(), config.getVersion());
        } else {
          startPromise.fail(http.cause());
        }
      });
  }

  private static Handler<RoutingContext> handleFailure() {
    return errorContext -> {
      if (errorContext.response().ended()) {
        // Ignore
        return;
      }
      LOGGER.error("Route error: ", errorContext.failure());
      errorContext.response()
        .setStatusCode(500)
        .end(new JsonObject().put("message", "Something went wrong!!!").toBuffer());
    };
  }
}
