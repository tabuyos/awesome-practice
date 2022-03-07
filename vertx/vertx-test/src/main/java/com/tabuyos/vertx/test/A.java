/*
 * Copyright (c) 2018-2022 Tabuyos All Right Reserved.
 */
package com.tabuyos.vertx.test;

import org.springframework.stereotype.Component;

/**
 * A
 *
 * @author tabuyos
 * @since 2022/3/4
 */
@Component
public class A extends AbstractPerson{

  public A() {
    System.out.println("A:");
    System.out.println(this.hashCode());
    sayMan();
  }
}
