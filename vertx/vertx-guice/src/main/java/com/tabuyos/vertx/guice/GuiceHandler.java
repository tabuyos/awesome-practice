/*
 * copyright(c) 2018-2022 tabuyos all right reserved.
 */
package com.tabuyos.vertx.guice;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import io.vertx.ext.web.RoutingContext;

/**
 * Handler
 *
 * @author tabuyos
 * @since 2022/2/21
 */
@Singleton
public class GuiceHandler {

  private final GuiceService guiceService;

  @Inject
  public GuiceHandler(GuiceService guiceService) {
    this.guiceService = guiceService;
  }

  public void guice(RoutingContext rc) {
    guiceService.say();
    rc.response().end("hello tabuyos");
  }
}
