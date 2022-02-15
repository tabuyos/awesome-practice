/*
 * Copyright (c) 2018-2022 Tabuyos All Right Reserved.
 */
package com.tabuyos.camel.test;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * MySpringBean
 *
 * @author tabuyos
 * @since 2022/2/14
 */
@Component("myBean")
public class MySpringBean {

  @Value("${greeting}")
  private String say;

  public String saySomething() {
    return say;
  }

}
