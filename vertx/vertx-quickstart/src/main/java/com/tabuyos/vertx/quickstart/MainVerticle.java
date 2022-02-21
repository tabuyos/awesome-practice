package com.tabuyos.vertx.quickstart;

import com.tabuyos.vertx.quickstart.model.Whisky;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Promise;
import io.vertx.core.json.Json;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import org.apache.logging.log4j.core.config.Configurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * MainVerticle
 *
 * @author tabuyos
 * @since 2022/2/17
 */
public class MainVerticle extends AbstractVerticle {

  private static final Logger logger = LoggerFactory.getLogger(MainVerticle.class);

  public static void main(String[] args) {
    System.setProperty("log4j.skipJansi", "false");
    Configurator.initialize(null, "log4j2-dev.yml");
    Runner.run(MainVerticle.class);
    logger.info("fdsafdsa");
    logger.error("fdsafdsa");
  }

  @Override
  public void start(Promise<Void> startPromise) throws Exception {
    createSomeProducts();
    // 创建一个router对象。
    Router router = Router.router(vertx);

    router.get("/api/whiskies").handler(this::getAll);
    router.route("/api/whiskies*").handler(BodyHandler.create());
    router.post("/api/whiskies").handler(this::addOne);
    router.delete("/api/whiskies/:id").handler(this::deleteOne);
    router.get().handler(request ->
                           request
                             .response()
                             .putHeader("content-type", "text/plain")
                             .end("Hello from Vert.x!"));

    vertx.deployVerticle(MyVerticle.class, new DeploymentOptions());
    vertx.createHttpServer()
         .requestHandler(router)
         .listen(8888, http -> {
           if (http.succeeded()) {
             startPromise.complete();
             System.out.println("HTTP server started on port 8888");
           } else {
             startPromise.fail(http.cause());
           }
         });
  }

  /**
   * 存放我们的产品
   */
  private Map<Integer, Whisky> products = new LinkedHashMap<>();

  /**
   * 创建一些产品
   */
  private void createSomeProducts() {
    Whisky bowmore = new Whisky("Bowmore 15 Years Laimrig", "Scotland, Islay");
    products.put(bowmore.getId(), bowmore);
    Whisky talisker = new Whisky("Talisker 57° North", "Scotland, Island");
    products.put(talisker.getId(), talisker);
  }

  private void getAll(RoutingContext routingContext) {
    routingContext.response()
                  .putHeader("Content-Type", "application/json; charset=utf-8")
                  .end(Json.encodePrettily(products.values()));
  }

  private void addOne(RoutingContext routingContext) {
    final Whisky whisky = Json.decodeValue(routingContext.getBodyAsString(),
                                           Whisky.class);
    products.put(whisky.getId(), whisky);
    routingContext.response()
                  .setStatusCode(201)
                  .putHeader("content-type", "application/json; charset=utf-8")
                  .end(Json.encodePrettily(whisky));
  }

  private void deleteOne(RoutingContext routingContext) {
    String id = routingContext.request().getParam("id");
    if (id == null) {
      routingContext.response().setStatusCode(400).end();
    } else {
      Integer idAsInteger = Integer.valueOf(id);
      products.remove(idAsInteger);
    }
    routingContext.response().setStatusCode(204).end();
  }
}
