/*
 * Copyright (c) 2018-2022 Tabuyos All Right Reserved.
 */
package com.tabuyos.nacos.springboot.config;

import org.springframework.boot.context.properties.ConfigurationBeanFactoryMetadata;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * NacosConfiguration
 *
 * @author tabuyos
 * @since 2022/1/7
 */
@Configuration
public class NacosConfiguration {

  @Bean("configurationBeanFactoryMetadata")
  public ConfigurationBeanFactoryMetadata beanFactoryMetadata() {
    return new ConfigurationBeanFactoryMetadata();
  }
}
