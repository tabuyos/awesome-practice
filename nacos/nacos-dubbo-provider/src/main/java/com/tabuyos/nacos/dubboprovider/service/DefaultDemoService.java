/*
 * Copyright (c) 2018-2022 Tabuyos All Right Reserved.
 */
package com.tabuyos.nacos.dubboprovider.service;

import com.tabuyos.nacos.dubbo.service.DemoService;
import org.apache.dubbo.config.annotation.DubboService;
import org.apache.dubbo.rpc.RpcContext;
import org.springframework.beans.factory.annotation.Value;

/**
 * DefaultDemoService
 *
 * @author tabuyos
 * @since 2022/1/10
 */
@DubboService(version = "${demo.service.version:1.0.0}")
public class DefaultDemoService implements DemoService {

  @Value("${demo.service.name:demoService}")
  private String serviceName;

  @Override
  public String sayName(String name) {
    RpcContext rpcContext = RpcContext.getServiceContext();
    return String.format(
        "Service [name :%s , port : %d] %s(\"%s\") : Hello,%s",
        serviceName,
        rpcContext.getLocalPort(),
        rpcContext.getMethodName(),
        name,
        name);
  }
}
