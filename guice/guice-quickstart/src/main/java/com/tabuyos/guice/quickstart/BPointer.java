/*
 * copyright(c) 2018-2022 tabuyos all right reserved.
 */
package com.tabuyos.guice.quickstart;

import com.google.inject.Inject;
import com.google.inject.Singleton;

/**
 * APointer
 *
 * @author tabuyos
 * @since 2022/2/21
 */
@Singleton
public class BPointer {

  private final CPointer cPointer;
  private final Vertx vertx;

  @Inject
  public BPointer(CPointer cPointer, Vertx vertx) {
    this.cPointer = cPointer;
    this.vertx = vertx;
  }

  public void say() {
    cPointer.say();
    System.out.println(vertx);
  }
}
