/*
 * Copyright (c) 2018-2022 Tabuyos All Right Reserved.
 */
package com.tabuyos.camel.test;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

/**
 * MySpringBootRouter
 *
 * @author tabuyos
 * @since 2022/2/14
 */
@Component
public class MySpringBootRouter extends RouteBuilder {

  @Override
  public void configure() {
    from("timer:hello?period=1000")
      .routeId("hello")
      .transform()
      .method("myBean", "saySomething")
      .filter(simple("${body} contains 'foo'"))
      .to("log:foo")
      .end()
      .filter(simple("${body} contains 'Hello'"))
      .to("log:hello")
      .end()
      .filter(simple("${body} contains 'Tabuyos'"))
      .to("log:tabuyos")
      .end()
      .to("stream:out");
  }

}
