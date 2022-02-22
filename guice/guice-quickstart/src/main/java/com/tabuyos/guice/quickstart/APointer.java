/*
 * copyright(c) 2018-2022 tabuyos all right reserved.
 */
package com.tabuyos.guice.quickstart;

import com.google.inject.Inject;

/**
 * APointer
 *
 * @author tabuyos
 * @since 2022/2/21
 */
public class APointer extends AbstractPointer{

  private final BPointer bPointer;

  @Inject
  public APointer(BPointer bPointer) {
    this.bPointer = bPointer;
  }

  public void say() {
    bPointer.say();
  }
}
