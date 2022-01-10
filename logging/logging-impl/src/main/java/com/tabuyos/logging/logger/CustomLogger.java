/*
 * Copyright (c) 2018-2022 Tabuyos All Right Reserved.
 */
package com.tabuyos.logging.logger;

import com.tabuyos.logging.appender.Appender;
import com.tabuyos.logging.context.LoggerContext;
import com.tabuyos.logging.enums.Level;
import com.tabuyos.logging.event.LoggingEvent;

import java.util.Arrays;

/**
 * CustomLogger
 *
 * @author tabuyos
 * @since 2022/1/10
 */
public class CustomLogger implements Logger {

  /**
   * logger name
   */
  private String name;
  /**
   * parent
   */
  private CustomLogger parent;
  /**
   * logger context
   */
  private LoggerContext loggerContext;
  /**
   * appender
   */
  private Appender appender;
  /**
   * 当前 Logger 的级别, 默认最低
   */
  private Level level = Level.TRACE;
  /**
   * 冗余级别字段, 方便使用
   */
  private int effectiveLevelInt;

  @Override
  public void trace(String message) {
    filterAndLog(Level.TRACE, message);
  }

  @Override
  public void debug(String message) {
    filterAndLog(Level.DEBUG, message);
  }

  @Override
  public void info(String message) {
    filterAndLog(Level.INFO, message);
  }

  @Override
  public void warn(String message) {
    filterAndLog(Level.WARN, message);
  }

  @Override
  public void error(String message) {
    filterAndLog(Level.ERROR, message);
  }

  /**
   * 过滤并输出，所有的输出方法都会调用此方法
   *
   * @param level   日志级别
   * @param message 输出内容
   */
  private void filterAndLog(Level level, String message) {
    StackTraceElement stackTraceElement;
    try {
      throw new RuntimeException();
    } catch (Throwable e) {
      System.out.println("----");
      Arrays.stream(e.getStackTrace()).forEach(System.out::println);
      System.out.println("----");
      stackTraceElement = e.getStackTrace()[2];
    }
    LoggingEvent event = LoggingEvent.of(level, message, getName());
    if (stackTraceElement != null) {
      Thread thread = Thread.currentThread();
      event = LoggingEvent.of(System.currentTimeMillis(),
                              level,
                              message,
                              thread.getName(),
                              thread.getId(),
                              getName(),
                              stackTraceElement.getLineNumber(),
                              stackTraceElement.getMethodName());
    }
    for (CustomLogger cl = this; cl != null; cl = cl.parent) {
      if (cl.appender == null) {
        continue;
      }
      if (level.code >= effectiveLevelInt) {
        cl.appender.append(event);
      }
    }
  }

  @Override
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public CustomLogger getParent() {
    return parent;
  }

  public void setParent(Logger parent) {
    this.parent = (CustomLogger) parent;
  }

  public LoggerContext getLoggerContext() {
    return loggerContext;
  }

  public void setLoggerContext(LoggerContext loggerContext) {
    this.loggerContext = loggerContext;
  }

  public Appender getAppender() {
    return appender;
  }

  public void setAppender(Appender appender) {
    this.appender = appender;
  }

  public Level getLevel() {
    return level;
  }

  public void setLevel(Level level) {
    this.level = level;
  }

  public int getEffectiveLevelInt() {
    return effectiveLevelInt;
  }

  public void setEffectiveLevelInt(int effectiveLevelInt) {
    this.effectiveLevelInt = effectiveLevelInt;
  }
}
