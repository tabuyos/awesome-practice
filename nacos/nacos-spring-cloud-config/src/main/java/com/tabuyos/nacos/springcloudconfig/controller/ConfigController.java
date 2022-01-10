/*
 * Copyright (c) 2018-2022 Tabuyos All Right Reserved.
 */
package com.tabuyos.nacos.springcloudconfig.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * ConfigController
 *
 * @author tabuyos
 * @since 2022/1/7
 */
@RestController
@RequestMapping("config")
@RefreshScope
public class ConfigController {

  @Value("${useLocalCache:false}")
  private boolean useLocalCache;

  @GetMapping("get")
  public boolean get() {
    return useLocalCache;
  }
}
