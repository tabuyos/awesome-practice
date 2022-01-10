/*
 * Copyright (c) 2018-2022 Tabuyos All Right Reserved.
 */
package com.tabuyos.logging.event;

import com.tabuyos.logging.enums.Level;

import java.util.StringJoiner;

/**
 * LoggingEvent
 *
 * @author tabuyos
 * @since 2022/1/10
 */
public class LoggingEvent {

  /**
   * 日志时间戳
   */
  public long timestamp;
  /**
   * 日志级别
   */
  private Level level;
  /**
   * 日志主题
   */
  private String message;
  /**
   * 线程名称
   */
  private String threadName;
  /**
   * 线程 ID
   */
  private long threadId;
  /**
   * 日志名称
   */
  private String loggerName;
  /**
   * 行号
   */
  private int lineNumber;
  /**
   * 方法名
   */
  private String methodName;

  public LoggingEvent(Level level, String message, String loggerName) {
    this.level = level;
    this.message = message;
    this.loggerName = loggerName;
  }

  public LoggingEvent(long timestamp,
                      Level level,
                      String message,
                      String threadName,
                      long threadId,
                      String loggerName,
                      int lineNumber,
                      String methodName) {
    this.timestamp = timestamp;
    this.level = level;
    this.message = message;
    this.threadName = threadName;
    this.threadId = threadId;
    this.loggerName = loggerName;
    this.lineNumber = lineNumber;
    this.methodName = methodName;
  }

  public static LoggingEvent of(Level level, String message, String loggerName) {
    return new LoggingEvent(level, message, loggerName);
  }

  public static LoggingEvent of(long timestamp,
                                Level level,
                                String message,
                                String threadName,
                                long threadId,
                                String loggerName,
                                int lineNumber,
                                String methodName) {
    return new LoggingEvent(timestamp, level, message, threadName, threadId, loggerName, lineNumber, methodName);
  }

  public long getTimestamp() {
    return timestamp;
  }

  public void setTimestamp(long timestamp) {
    this.timestamp = timestamp;
  }

  public Level getLevel() {
    return level;
  }

  public void setLevel(Level level) {
    this.level = level;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public String getThreadName() {
    return threadName;
  }

  public void setThreadName(String threadName) {
    this.threadName = threadName;
  }

  public long getThreadId() {
    return threadId;
  }

  public void setThreadId(long threadId) {
    this.threadId = threadId;
  }

  public String getLoggerName() {
    return loggerName;
  }

  public void setLoggerName(String loggerName) {
    this.loggerName = loggerName;
  }

  @Override
  public String toString() {
    return new StringJoiner(", ", LoggingEvent.class.getSimpleName() + "(", ")")
      .add("timestamp=" + timestamp)
      .add("level=" + level)
      .add("message='" + message + "'")
      .add("threadName='" + threadName + "'")
      .add("threadId=" + threadId)
      .add("loggerName='" + loggerName + "'")
      .add("lineNumber=" + lineNumber)
      .add("methodName='" + methodName + "'")
      .toString();
  }
}
