/*
 * Copyright (c) 2018-2022 Tabuyos All Right Reserved.
 */
package com.tabuyos.springcloudprovider;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * NacosSpringCloudProviderApplication
 *
 * @author tabuyos
 * @since 2022/1/7
 */
@SpringBootApplication
@EnableDiscoveryClient
public class NacosSpringCloudProviderApplication {

  public static void main(String[] args) {
    SpringApplication.run(NacosSpringCloudProviderApplication.class, args);
  }

  @RestController
  static class EchoController {
    @GetMapping("/echo/{string}")
    public String echo(@PathVariable String string) {
      return "Hello Nacos Discovery " + string;
    }
  }
}
