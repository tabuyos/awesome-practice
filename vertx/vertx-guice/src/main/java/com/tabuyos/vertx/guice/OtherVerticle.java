/*
 * copyright(c) 2018-2022 tabuyos all right reserved.
 */
package com.tabuyos.vertx.guice;

import com.google.inject.Guice;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;

/**
 * OtherVerticle
 *
 * @author tabuyos
 * @since 2022/2/21
 */
public class OtherVerticle extends AbstractVerticle {

  @Override
  public void start() throws Exception {
    System.out.println(111222);
    GuiceRouter instance = Guice.createInjector(binder -> binder.bind(Vertx.class).toInstance(vertx)).getInstance(GuiceRouter.class);
    instance.say();
  }
}
