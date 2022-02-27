/*
 * copyright(c) 2018-2022 tabuyos all right reserved.
 */
package com.tabuyos.vertx.servitization.servitization;

import com.tabuyos.vertx.servitization.servitization.verticle.ServitizationVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;

/**
 * ServitizationLauncher
 *
 * @author tabuyos
 * @since 2022/2/22
 */
public class ServitizationLauncher {

  public static void main(String[] args) {
    Vertx vertx = Vertx.vertx();
    vertx.deployVerticle(ServitizationVerticle.class, new DeploymentOptions());
  }
}
