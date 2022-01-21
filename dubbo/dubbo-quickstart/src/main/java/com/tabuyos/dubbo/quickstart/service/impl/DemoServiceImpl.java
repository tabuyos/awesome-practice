/*
 * Copyright (c) 2018-2022 Tabuyos All Right Reserved.
 */
package com.tabuyos.dubbo.quickstart.service.impl;

import com.tabuyos.dubbo.quickstart.service.DemoService;
import org.apache.dubbo.config.annotation.DubboService;
import org.apache.dubbo.rpc.RpcContext;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * DemoServiceImpl
 *
 * @author tabuyos
 * @since 2022/1/21
 */
@DubboService(version = "${demo.service.version:1.0.0}")
public class DemoServiceImpl implements DemoService {

  @Override
  public String sayHello(String name) {
    System.out.println("[" + new SimpleDateFormat("HH:mm:ss").format(new Date()) + "] Hello " + name +
                       ", request from consumer: " + RpcContext.getServiceContext().getLocalAddress());
    return "Hello " + name + ", response from provider: " + RpcContext.getServiceContext().getLocalAddress();
  }
}
