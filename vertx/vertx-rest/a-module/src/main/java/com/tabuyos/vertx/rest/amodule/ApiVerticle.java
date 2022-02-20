/*
 * copyright(c) 2018-2022 tabuyos all right reserved.
 */
package com.tabuyos.vertx.rest.amodule;

import com.tabuyos.vertx.rest.dmodule.RestRouter;

import java.util.ServiceLoader;

/**
 * ApiVerticle
 *
 * @author tabuyos
 * @since 2022/2/19
 */
public class ApiVerticle extends ApplicationVerticle {

  @Override
  public void start() throws Exception {
    ServiceLoader<RestRouter> restRouters = ServiceLoader.load(RestRouter.class);
    restRouters.forEach(RestRouter::init);
  }
}
