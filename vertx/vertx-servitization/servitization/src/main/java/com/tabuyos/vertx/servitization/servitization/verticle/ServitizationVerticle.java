/*
 * copyright(c) 2018-2022 tabuyos all right reserved.
 */
package com.tabuyos.vertx.servitization.servitization.verticle;

import io.vertx.core.AbstractVerticle;
import io.vertx.ext.web.Router;

/**
 * ServitizationVerticle
 *
 * @author tabuyos
 * @since 2022/2/24
 */
public class ServitizationVerticle extends AbstractVerticle {
  @Override
  public void start() throws Exception {
    Router router = Router.router(vertx);
    router.get("/a")
      .handler(rc -> rc.response().end("a"));
    router.get("/b")
      .handler(rc -> rc.response().end("b"));

    Router subRouter = Router.router(vertx);
    subRouter.get("/a")
      .handler(rc -> rc.response().end("a"));
    subRouter.get("/b")
      .handler(rc -> rc.response().end("b"));

    router.mountSubRouter("/c", subRouter);
  }
}
