/*
 * copyright(c) 2018-2022 tabuyos all right reserved.
 */
package com.tabuyos.vertx.rest.dmodule;

import io.vertx.core.Vertx;
import io.vertx.ext.web.Router;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Registry
 *
 * @author tabuyos
 * @since 2022/2/19
 */
public class Registry {

  private static final Map<String, RestRouter> REST_ROUTER_MAP = new ConcurrentHashMap<>();

  public static boolean registry(String path, RestRouter router) {
    if (path == null) {
      System.out.println("path is null!");
      return false;
    }
    RestRouter restRouter = REST_ROUTER_MAP.putIfAbsent(path, router);
    return restRouter == null;
  }

  public static void mountRouter(Vertx vertx, Router router) {
    REST_ROUTER_MAP.forEach((path, restRouter) -> {
      System.out.println("mount pointer: " + path);
      router.mountSubRouter(path, restRouter.buildRouter(vertx));
    });
  }
}
