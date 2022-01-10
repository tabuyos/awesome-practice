/*
 * Copyright (c) 2018-2022 Tabuyos All Right Reserved.
 */
package com.tabuyos.logging.factory;

import com.tabuyos.logging.logger.Logger;

/**
 * ILoggerFactory
 *
 * @author tabuyos
 * @since 2022/1/10
 */
public interface ILoggerFactory {

  /**
   * 通过class获取/创建logger
   *
   * @param clazz logger class
   * @return logger
   */
  Logger getLogger(Class<?> clazz);

  /**
   * 通过name获取/创建logger
   *
   * @param name name
   * @return logger
   */
  Logger getLogger(String name);

  /**
   * 通过name创建logger
   *
   * @param name name
   * @return logger
   */
  Logger newLogger(String name);
}
