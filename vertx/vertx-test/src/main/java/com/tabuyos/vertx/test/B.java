/*
 * Copyright (c) 2018-2022 Tabuyos All Right Reserved.
 */
package com.tabuyos.vertx.test;

import org.springframework.stereotype.Component;

/**
 * B
 *
 * @author tabuyos
 * @since 2022/3/4
 */
@Component
public class B extends AbstractPerson{

  public B() {
    System.out.println("B:");
    System.out.println(this.hashCode());
    sayMan();
  }
}
