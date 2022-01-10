/*
 * Copyright (c) 2018-2022 Tabuyos All Right Reserved.
 */
package com.tabuyos.logging.factory;

import com.tabuyos.logging.logger.Logger;

/**
 * LoggerFactory
 *
 * @author tabuyos
 * @since 2022/1/10
 */
public class LoggerFactory {

  private static final ILoggerFactory loggerFactory = new StaticLoggerFactory();

  public static ILoggerFactory getLoggerFactory() {
    return loggerFactory;
  }

  public static Logger getLogger(Class<?> clazz) {
    return getLoggerFactory().getLogger(clazz);
  }

  public static Logger getLogger(String name) {
    return getLoggerFactory().getLogger(name);
  }
}
