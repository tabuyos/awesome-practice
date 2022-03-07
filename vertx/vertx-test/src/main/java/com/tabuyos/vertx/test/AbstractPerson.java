/*
 * Copyright (c) 2018-2022 Tabuyos All Right Reserved.
 */
package com.tabuyos.vertx.test;

import javax.annotation.PostConstruct;

/**
 * AbstractPerson
 *
 * @author tabuyos
 * @since 2022/3/4
 */
public abstract class AbstractPerson {

  private Man man;

  public final void sayMan() {
    System.out.println(man);
  }

  @PostConstruct
  public final void init() {
    man = new Man();
  }
}
