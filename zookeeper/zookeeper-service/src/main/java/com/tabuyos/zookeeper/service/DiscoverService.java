/*
 * Copyright (c) 2018-2021 Tabuyos All Right Reserved.
 */
package com.tabuyos.zookeeper.service;

/**
 * DiscoverService
 *
 * @author tabuyos
 * @since 2021/12/30
 */
@FunctionalInterface
public interface DiscoverService extends CloseService {

  /**
   * discover
   *
   * @param key key
   * @return value
   */
  String discover(String key);
}
