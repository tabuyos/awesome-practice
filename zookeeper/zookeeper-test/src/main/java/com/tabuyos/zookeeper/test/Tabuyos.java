/*
 * Copyright (c) 2018-2021 Tabuyos All Right Reserved.
 */
package com.tabuyos.zookeeper.test;

import com.tabuyos.zookeeper.service.DiscoverService;
import com.tabuyos.zookeeper.service.RegistryService;
import com.tabuyos.zookeeper.service.RemoveService;
import com.tabuyos.zookeeper.service.impl.DiscoverServiceImpl;
import com.tabuyos.zookeeper.service.impl.RegistryServiceImpl;
import com.tabuyos.zookeeper.service.impl.RemoveServiceImpl;

import java.util.concurrent.TimeUnit;

/**
 * Tabuyos
 *
 * @author tabuyos
 * @since 2021/12/30
 */
public class Tabuyos {

  static DiscoverService discoverService;
  static RegistryService registryService;
  static RemoveService removeService;

  public static void main(String[] args) throws Exception {
    String address = "127.0.0.1:2181";
    registryService = new RegistryServiceImpl(address);
    registryService.registry("rpc", "tabuyos4");
    registryService.registry("rpc", "tabuyos0");
    registryService.registry("rpc2", "tabuyos2");
    TimeUnit.SECONDS.sleep(10);
    registryService.close();

    discoverService = new DiscoverServiceImpl(address);
    String discover = discoverService.discover("rpc");

    removeService = new RemoveServiceImpl(address);
    removeService.remove("rpc");

    discoverService = new DiscoverServiceImpl(address);
    String discover1 = discoverService.discover("rpc");
    String discover2 = discoverService.discover("rpc2");
    System.out.println("==================================");
    System.out.println(discover);
    System.out.println("---------");
    System.out.println(discover1);
    System.out.println("+++++++++");
    System.out.println(discover2);
    System.out.println("==================================");
  }
}
