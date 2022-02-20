/*
 * copyright(c) 2018-2022 tabuyos all right reserved.
 */
package com.tabuyos.vertx.rest.bmodule;

import com.tabuyos.vertx.rest.dmodule.Registry;
import com.tabuyos.vertx.rest.dmodule.RestRouter;
import io.vertx.core.Vertx;
import io.vertx.ext.web.Router;

/**
 * BRouter
 *
 * @author tabuyos
 * @since 2022/2/19
 */
public class BRouter implements RestRouter {
  @Override
  public Router buildRouter(Vertx vertx) {
    Router router = Router.router(vertx);
    router.get("/b").handler(rc -> {
      rc.response()
        .end("my name is B ");
    });
    return router;
  }

  @Override
  public void init() {
    if (!Registry.registry("/bmodule", this)) {
      System.out.println("registry fail.");
    }
  }
}
