/*
 * Copyright (c) 2018-2022 Tabuyos All Right Reserved.
 */
package com.tabuyos.dubbo.quickstart;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.PropertySource;

import java.io.IOException;

/**
 * DemoServiceProviderBootstrap
 *
 * @author tabuyos
 * @since 2022/1/21
 */
@EnableDubbo(scanBasePackages = {"com.tabuyos.dubbo.quickstart.service"})
@PropertySource(value = "classpath:/provider-config.properties")
public class DemoServiceProviderBootstrap {

  public static void main(String[] args) throws IOException {
    AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
    context.register(DemoServiceProviderBootstrap.class);
    context.refresh();
    System.out.println("DemoService provider is starting...");
    System.in.read();
  }
}
