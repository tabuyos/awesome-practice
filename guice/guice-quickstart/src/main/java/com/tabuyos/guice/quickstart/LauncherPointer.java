/*
 * copyright(c) 2018-2022 tabuyos all right reserved.
 */
package com.tabuyos.guice.quickstart;

import com.google.inject.Binder;
import com.google.inject.Guice;
import com.google.inject.Module;

/**
 * LauncherPointer
 *
 * @author tabuyos
 * @since 2022/2/21
 */
public class LauncherPointer {

  public static void main(String[] args) {
    APointer instance = Guice.createInjector(new Module() {
      @Override
      public void configure(Binder binder) {
        binder.bind(Vertx.class).toInstance(new VertxImpl());
      }
    }).getInstance(APointer.class);
    instance.say();
  }
}
