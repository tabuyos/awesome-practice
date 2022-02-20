/*
 * copyright(c) 2018-2022 tabuyos all right reserved.
 */
package com.tabuyos.vertx.rest.amodule;

import com.tabuyos.vertx.rest.dmodule.Registry;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.ext.web.Router;

/**
 * ApplicationVerticle
 *
 * @author tabuyos
 * @since 2022/2/19
 */
public class ApplicationVerticle extends AbstractVerticle {

  @Override
  public void start(Promise<Void> startPromise) throws Exception {

    ApiVerticle apiVerticle = new ApiVerticle();
    apiVerticle.start();

    Router router = Router.router(vertx);

    router.route("/*").handler(rc -> {
      System.out.println(rc.request().remoteAddress().host());
      rc.next();
    });

    Registry.mountRouter(vertx, router);

    router.getRoutes().forEach(route -> System.out.println(route.getPath()));

    vertx
      .createHttpServer()
      .requestHandler(router)
      .listen(8888)
      .onComplete(server -> {
        if (server.succeeded()) {
          startPromise.complete();
          System.out.println("application start successful.");
        } else {
          startPromise.fail(server.cause());
          System.out.println("application start failed.");
        }
      });
  }
}
