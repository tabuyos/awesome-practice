/*
 * copyright(c) 2018-2022 tabuyos all right reserved.
 */
package com.tabuyos.vertx.guice;

import com.google.inject.Binder;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Module;
import com.google.inject.Singleton;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;

/**
 * MainVerticle
 *
 * @author tabuyos
 * @since 2022/2/21
 */
@Singleton
public class MainVerticle extends AbstractVerticle {

  private final GuiceRouter router = null;
//
//  @Inject
//  public MainVerticle(GuiceRouter router) {
//    this.router = router;
//  }

  public void say() {
    System.out.println("router");
  }

  @Override
  public void start() throws Exception {
    System.out.println(123);
    say();

//    MainVerticle instance = Guice.createInjector(binder -> binder.bind(Vertx.class).toInstance(vertx)).getInstance(MainVerticle.class);
//    instance.say();
//    instance.buildRouter();
//    System.out.println(router);
    vertx.deployVerticle(OtherVerticle.class, new DeploymentOptions());
  }
}
