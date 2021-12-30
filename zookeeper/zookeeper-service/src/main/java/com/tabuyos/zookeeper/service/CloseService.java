/*
 * Copyright (c) 2018-2021 Tabuyos All Right Reserved.
 */
package com.tabuyos.zookeeper.service;

import java.io.Closeable;
import java.io.IOException;

/**
 * CloseService
 *
 * @author tabuyos
 * @since 2021/12/30
 */
public interface CloseService extends Closeable {

  /**
   * close
   *
   * @throws IOException io ex
   */
  @Override
  default void close() throws IOException {}
}
