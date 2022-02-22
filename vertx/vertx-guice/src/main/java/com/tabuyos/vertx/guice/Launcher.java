/*
 * copyright(c) 2018-2022 tabuyos all right reserved.
 */
package com.tabuyos.vertx.guice;

import com.google.inject.Binder;
import com.google.inject.Guice;
import com.google.inject.Module;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;

/**
 * Launcher
 *
 * @author tabuyos
 * @since 2022/2/21
 */
public class Launcher {

  public static void main(String[] args) {
    Vertx vertx = Vertx.vertx();
    System.out.println(vertx);

    vertx.deployVerticle(MainVerticle.class, new DeploymentOptions());
  }
}
