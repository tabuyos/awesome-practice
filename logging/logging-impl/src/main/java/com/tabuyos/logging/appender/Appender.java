/*
 * Copyright (c) 2018-2022 Tabuyos All Right Reserved.
 */
package com.tabuyos.logging.appender;

import com.tabuyos.logging.event.LoggingEvent;

/**
 * Appender
 *
 * @author tabuyos
 * @since 2022/1/10
 */
@FunctionalInterface
public interface Appender {

  /**
   * append event
   *
   * @param event event
   */
  void append(LoggingEvent event);
}
