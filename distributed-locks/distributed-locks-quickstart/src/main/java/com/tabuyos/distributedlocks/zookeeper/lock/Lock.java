/*
 * Copyright (c) 2018-2022 Tabuyos All Right Reserved.
 */
package com.tabuyos.distributedlocks.zookeeper.lock;

/**
 * Lock
 *
 * @author tabuyos
 * @since 2022/5/17
 */
public interface Lock {

  /**
   * 加锁方法
   *
   * @return 是否成功加锁
   * @throws Exception e
   */
  boolean lock() throws Exception;

  /**
   * 解锁方法
   *
   * @return 是否成功解锁
   * @throws Exception e
   */
  boolean unlock() throws Exception;
}
