/*
 * Copyright (c) 2018-2022 Tabuyos All Right Reserved.
 */
package com.tabuyos.nacos.dubboprovider;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * NacosDubboApplication
 *
 * @author tabuyos
 * @since 2022/1/10
 */
@EnableDubbo(scanBasePackages = {"com.tabuyos.nacos.dubbo.service"})
@SpringBootApplication
public class NacosDubboApplication {

  public static void main(String[] args) {
    SpringApplication.run(NacosDubboApplication.class, args);
  }
}
