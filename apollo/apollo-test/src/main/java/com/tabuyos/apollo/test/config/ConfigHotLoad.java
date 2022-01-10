/*
 * Copyright (c) 2018-2022 Tabuyos All Right Reserved.
 */
package com.tabuyos.apollo.test.config;

import com.ctrip.framework.apollo.core.ConfigConsts;
import com.ctrip.framework.apollo.model.ConfigChangeEvent;
import com.ctrip.framework.apollo.spring.annotation.ApolloConfigChangeListener;
import org.springframework.context.annotation.Configuration;

/**
 * ConfigHotLoad
 *
 * @author tabuyos
 * @since 2022/1/5
 */
@Configuration
public class ConfigHotLoad {

  @ApolloConfigChangeListener(
      value = {ConfigConsts.NAMESPACE_APPLICATION, "dev"},
      interestedKeyPrefixes = {"spring.datasource"})
  public void onChange(ConfigChangeEvent configChangeEvent) {
    System.out.println(123);
  }
}
