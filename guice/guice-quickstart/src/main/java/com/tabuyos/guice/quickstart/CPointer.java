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
public class CPointer{

  public void say() {
    System.out.println(111);
  }
}
