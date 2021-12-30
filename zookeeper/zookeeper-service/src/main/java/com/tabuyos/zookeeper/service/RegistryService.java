/*
 * Copyright (c) 2018-2021 Tabuyos All Right Reserved.
 */
package com.tabuyos.zookeeper.service;

/**
 * RegistryService
 *
 * @author tabuyos
 * @since 2021/12/30
 */
@FunctionalInterface
public interface RegistryService extends CloseService {

  /**
   * registry
   *
   * @param key key
   * @param value value
   * @throws Exception e
   */
  void registry(String key, String value) throws Exception;
}
