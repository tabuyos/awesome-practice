/*
 * Copyright (c) 2018-2022 Tabuyos All Right Reserved.
 */
package com.tabuyos.logging.logger;

/**
 * Logger
 *
 * @author tabuyos
 * @since 2022/1/10
 */
public interface Logger {

  /**
   * trace
   * @param message message
   */
  void trace(String message);

  /**
   * debug
   * @param message message
   */
  void debug(String message);

  /**
   * info
   * @param message message
   */
  void info(String message);

  /**
   * warn
   * @param message message
   */
  void warn(String message);

  /**
   * error
   * @param message message
   */
  void error(String message);

  String getName();
}
