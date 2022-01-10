/*
 * Copyright (c) 2018-2022 Tabuyos All Right Reserved.
 */
package com.tabuyos.nacos.springboot;

import com.alibaba.nacos.spring.context.annotation.config.NacosPropertySource;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * NacosSpringBootApplication
 *
 * @author tabuyos
 * @since 2022/1/7
 */
@SpringBootApplication
@NacosPropertySource(dataId = "example", autoRefreshed = true)
public class NacosSpringBootApplication {

  public static void main(String[] args) {
    SpringApplication.run(NacosSpringBootApplication.class, args);
  }
}
