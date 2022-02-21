/*
 * Copyright (c) 2018-2022 Tabuyos All Right Reserved.
 */
package com.tabuyos.vertx.quickstart;

import io.vertx.core.AbstractVerticle;
import io.vertx.ext.web.Router;

/**
 * MyVerticle
 *
 * @author tabuyos
 * @since 2022/2/17
 */
public class MyVerticle extends AbstractVerticle {

  /**
   * Verticle部署时调用
   */
  @Override
  public void start() {
    Router router = Router.router(vertx);
    router.get("/tabuyos").handler(request ->
                           request
                             .response()
                             .putHeader("content-type", "text/plain")
                             .end("Hello tabuyos"));
    System.out.println(MyVerticle.class.getName().concat(" start()"));
  }

  /**
   * 可选 - Verticle撤销时调用
   */
  @Override
  public void stop() {
    System.out.println(MyVerticle.class.getName().concat(" stop()"));
  }
}
