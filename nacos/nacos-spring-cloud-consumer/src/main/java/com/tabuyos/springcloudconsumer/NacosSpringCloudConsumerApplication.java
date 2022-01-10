/*
 * Copyright (c) 2018-2022 Tabuyos All Right Reserved.
 */
package com.tabuyos.springcloudconsumer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

/**
 * NacosSpringCloudConsumerApplication
 *
 * @author tabuyos
 * @since 2022/1/7
 */
@SpringBootApplication
@EnableDiscoveryClient
public class NacosSpringCloudConsumerApplication {

  @LoadBalanced
  @Bean
  public RestTemplate restTemplate() {
    return new RestTemplate();
  }

  public static void main(String[] args) {
    SpringApplication.run(NacosSpringCloudConsumerApplication.class, args);
  }

  @RestController
  static class TestController {

    private final RestTemplate restTemplate;

    @Autowired
    public TestController(RestTemplate restTemplate) {
      this.restTemplate = restTemplate;
    }

    @GetMapping("/echo/{str}")
    public String echo(@PathVariable String str) {
      return restTemplate.getForObject("http://service-provider/echo/" + str, String.class);
    }
  }
}
