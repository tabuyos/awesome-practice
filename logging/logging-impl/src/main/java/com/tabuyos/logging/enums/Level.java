/*
 * Copyright (c) 2018-2022 Tabuyos All Right Reserved.
 */
package com.tabuyos.logging.enums;

/**
 * Level
 *
 * @author tabuyos
 * @since 2022/1/10
 */
public enum Level {
  /**
   * error
   */
  ERROR(40000, "ERROR"),
  /**
   * warn
   */
  WARN(30000, "WARN"),
  /**
   * info
   */
  INFO(20000, "INFO"),
  /**
   * debug
   */
  DEBUG(10000, "DEBUG"),
  /**
   * trace
   */
  TRACE(5000, "TRACE");

  /**
   * code
   */
  public final int code;
  /**
   * level
   */
  public final String level;

  Level(int code, String level) {
    this.code = code;
    this.level = level;
  }

  public static Level parse(String level) {
    return valueOf(level.toUpperCase());
  }

  public boolean isGreaterOrEqual(Level level) {
    return code >= level.code;
  }

}
