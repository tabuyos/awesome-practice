/*
 * copyright(c) 2018-2022 tabuyos all right reserved.
 */
package com.tabuyos.vertx.guice;

import com.google.inject.Inject;
import io.vertx.core.Vertx;

/**
 * Service
 *
 * @author tabuyos
 * @since 2022/2/21
 */
public class GuiceService {

  private final Vertx vertx;

  @Inject
  public GuiceService(Vertx vertx) {
    this.vertx = vertx;
    System.out.println(vertx);
    System.out.println(Vertx.currentContext().config());
    System.out.println(vertx.getOrCreateContext().config());
  }

  public void say() {
    System.out.println(vertx);
    System.out.println(12321);
  }
}
