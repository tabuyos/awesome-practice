/*
 * Copyright (c) 2018-2021 Tabuyos All Right Reserved.
 */
package com.tabuyos.zookeeper.service;

/**
 * RemoveService
 *
 * @author tabuyos
 * @since 2021/12/30
 */
@FunctionalInterface
public interface RemoveService extends CloseService {

  /**
   * remove
   *
   * @param key key
   * @throws Exception ex
   */
  void remove(String key) throws Exception;
}
