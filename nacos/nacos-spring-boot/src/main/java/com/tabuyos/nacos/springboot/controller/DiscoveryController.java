/*
 * Copyright (c) 2018-2022 Tabuyos All Right Reserved.
 */
package com.tabuyos.nacos.springboot.controller;

import com.alibaba.nacos.api.annotation.NacosInjected;
import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.pojo.Instance;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * DiscoveryController
 *
 * @author tabuyos
 * @since 2022/1/7
 */
@RestController
@RequestMapping("discovery")
public class DiscoveryController {

  @NacosInjected private NamingService namingService;

  @GetMapping("/get")
  @ResponseBody
  public List<Instance> get(@RequestParam String serviceName) throws NacosException {
    return namingService.getAllInstances(serviceName);
  }
}
