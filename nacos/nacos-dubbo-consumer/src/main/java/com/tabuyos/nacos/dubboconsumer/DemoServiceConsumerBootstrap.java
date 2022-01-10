/*
 * Copyright (c) 2018-2022 Tabuyos All Right Reserved.
 */
package com.tabuyos.nacos.dubboconsumer;

import com.tabuyos.nacos.dubbo.service.DemoService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.PropertySource;

import javax.annotation.PostConstruct;
import java.io.IOException;

/**
 * DemoServiceConsumerBootstrap
 *
 * @author tabuyos
 * @since 2022/1/10
 */
@EnableDubbo
@PropertySource(value = "classpath:/consumer-config.properties")
public class DemoServiceConsumerBootstrap {
  @DubboReference(version = "${demo.service.version}")
  private DemoService demoService;

  @PostConstruct
  public void init() {
    for (int i = 0; i < 50; i++) {
      System.out.println(demoService.sayName("tabuyos-" + i));
    }
  }

  public static void main(String[] args) throws IOException {
    AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
    context.register(DemoServiceConsumerBootstrap.class);
    context.refresh();
    // context.close();
    System.in.read();
  }
}
