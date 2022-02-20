/*
 * copyright(c) 2018-2022 tabuyos all right reserved.
 */
package com.tabuyos.vertx.rest.cmodule;

import com.tabuyos.vertx.rest.dmodule.Registry;
import com.tabuyos.vertx.rest.dmodule.RestRouter;
import io.vertx.core.Vertx;
import io.vertx.ext.web.Router;

/**
 * CRouter
 *
 * @author tabuyos
 * @since 2022/2/19
 */
public class CRouter implements RestRouter {
  @Override
  public Router buildRouter(Vertx vertx) {
    Router router = Router.router(vertx);
    router.get("/c").handler(rc -> {
      rc.response()
        .end("my name is C");
    });
    return router;
  }

  @Override
  public void init() {
    if (!Registry.registry("/cmodule", this)) {
      System.out.println("registry fail.");
    }
  }
}
