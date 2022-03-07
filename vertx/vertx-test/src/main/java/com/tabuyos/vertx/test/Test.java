/*
 * Copyright (c) 2018-2022 Tabuyos All Right Reserved.
 */
package com.tabuyos.vertx.test;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * Test
 *
 * @author tabuyos
 * @since 2022/3/4
 */
@SpringBootApplication
public class Test {

  public static void main(String[] args) {
    ConfigurableApplicationContext context = SpringApplication.run(Test.class, args);
    A bean = context.getBean(A.class);
    bean.sayMan();
    System.out.println(bean.hashCode());
    context.stop();
  }
}
