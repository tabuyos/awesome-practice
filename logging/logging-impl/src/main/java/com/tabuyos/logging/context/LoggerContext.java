/*
 * Copyright (c) 2018-2022 Tabuyos All Right Reserved.
 */
package com.tabuyos.logging.context;

import com.tabuyos.logging.logger.Logger;

import java.util.HashMap;
import java.util.Map;

/**
 * LoggerContext
 *
 * @author tabuyos
 * @since 2022/1/10
 */
public class LoggerContext {

  /**
   * 根 logger
   */
  private Logger root;

  /**
   * logger缓存，存放解析配置文件后生成的logger对象，以及通过程序手动创建的logger对象
   */
  private Map<String, Logger> loggerCache = new HashMap<>();

  public void addLogger(String name, Logger logger) {
    loggerCache.put(name, logger);
  }

  public void addLogger(Logger logger) {
    loggerCache.put(logger.getName(), logger);
  }

  public Logger getRoot() {
    return root;
  }

  public void setRoot(Logger root) {
    this.root = root;
  }

  public Map<String, Logger> getLoggerCache() {
    return loggerCache;
  }

  public void setLoggerCache(Map<String, Logger> loggerCache) {
    this.loggerCache = loggerCache;
  }
}
