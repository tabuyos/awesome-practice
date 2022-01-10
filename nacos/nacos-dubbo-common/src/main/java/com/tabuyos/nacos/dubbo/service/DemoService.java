/*
 * Copyright (c) 2018-2022 Tabuyos All Right Reserved.
 */
package com.tabuyos.nacos.dubbo.service;

/**
 * DemoService
 *
 * @author tabuyos
 * @since 2022/1/10
 */
public interface DemoService {

  /**
   * say name
   *
   * @param name name
   * @return string of message
   */
  String sayName(String name);
}
