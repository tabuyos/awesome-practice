/*
 * copyright(c) 2018-2022 tabuyos all right reserved.
 */
package com.tabuyos.guice.quickstart;

import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;

/**
 * Simple
 *
 * @author tabuyos
 * @since 2022/2/20
 */
@Singleton
public class Simple {

  @Inject
  private IHelloPrinter iHelloPrinter;

  public void hello() {
    iHelloPrinter.print();
  }

  public static void main(String[] args) {
    Injector injector = Guice.createInjector(new SimpleModule());
    Simple instance = injector.getInstance(Simple.class);
    instance.hello();
  }
}
