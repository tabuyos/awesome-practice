/*
 * copyright(c) 2018-2022 tabuyos all right reserved.
 */
package com.tabuyos.vertx.rest.dmodule;

import io.vertx.core.Vertx;
import io.vertx.ext.web.Router;

/**
 * RestRouter
 *
 * @author tabuyos
 * @since 2022/2/19
 */
public interface RestRouter {

  Router buildRouter(Vertx vertx);

  void init();
}
