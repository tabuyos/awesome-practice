/*
 * Copyright (c) 2018-2022 Tabuyos All Right Reserved.
 */
package com.tabuyos.quarkus.quickstart;

import javax.enterprise.context.ApplicationScoped;

/**
 * GreetingService
 *
 * @author tabuyos
 * @since 2022/2/15
 */

@ApplicationScoped
public class GreetingService {

  public String greeting(String name) {
    return "hello " + name;
  }
}
