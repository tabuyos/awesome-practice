/*
 * copyright(c) 2018-2022 tabuyos all right reserved.
 */
package com.tabuyos.vertx.guice;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import io.vertx.core.Vertx;
import io.vertx.ext.web.Router;

/**
 * Router
 *
 * @author tabuyos
 * @since 2022/2/21
 */
@Singleton
public class GuiceRouter {

  private final Vertx vertx;
  private final GuiceHandler guiceHandler;

  @Inject
  public GuiceRouter(Vertx vertx, GuiceHandler guiceHandler) {
    System.out.println(111);
    System.out.println(vertx);
    System.out.println(vertx.getOrCreateContext());
    this.vertx = vertx;
    this.guiceHandler = guiceHandler;
  }

  public void say() {
    System.out.println(3333);
  }

  public void buildRouter() {
    System.out.println(888);
//    Router router = Router.router(vertx);
//    System.out.println(vertx);
//    router.get("/guice")
//      .handler(guiceHandler::guice);
  }
}
