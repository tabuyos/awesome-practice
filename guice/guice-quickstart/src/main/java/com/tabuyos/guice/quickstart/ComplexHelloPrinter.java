/*
 * copyright(c) 2018-2022 tabuyos all right reserved.
 */
package com.tabuyos.guice.quickstart;

import com.google.inject.Singleton;

/**
 * ComplexHelloPrinter
 *
 * @author tabuyos
 * @since 2022/2/20
 */
@Singleton
public class ComplexHelloPrinter implements IHelloPrinter{

  @Override
  public void print() {
    System.out.println("complex hello printer");
  }
}
