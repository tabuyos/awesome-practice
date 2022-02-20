/*
 * copyright(c) 2018-2022 tabuyos all right reserved.
 */
package com.tabuyos.guice.quickstart;

import com.google.inject.AbstractModule;

/**
 * SampleModule
 *
 * @author tabuyos
 * @since 2022/2/20
 */
public class SimpleModule extends AbstractModule {

  @Override
  protected void configure() {
    bind(IHelloPrinter.class).to(SimpleHelloPrinter.class);
  }
}
