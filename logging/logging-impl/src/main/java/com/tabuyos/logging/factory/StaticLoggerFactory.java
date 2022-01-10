/*
 * Copyright (c) 2018-2022 Tabuyos All Right Reserved.
 */
package com.tabuyos.logging.factory;

import com.tabuyos.logging.appender.ConsoleAppender;
import com.tabuyos.logging.config.ContextInitializer;
import com.tabuyos.logging.context.LoggerContext;
import com.tabuyos.logging.logger.CustomLogger;
import com.tabuyos.logging.logger.Logger;

/**
 * StaticLoggerFactory
 *
 * @author tabuyos
 * @since 2022/1/10
 */
public class StaticLoggerFactory implements ILoggerFactory {

  /**
   * 引用LoggerContext
   */
  private LoggerContext loggerContext;

  public StaticLoggerFactory() {
    ContextInitializer.autoconfig();
    loggerContext = ContextInitializer.getDefaultLoggerContext();
  }

  @Override
  public Logger getLogger(Class<?> clazz) {
    return getLogger(clazz.getName());
  }

  @Override
  public Logger getLogger(String name) {
    Logger logger = loggerContext.getLoggerCache().get(name);
    if (logger == null) {
      logger = newLogger(name);
    }
    return logger;
  }

  /**
   * 创建Logger对象
   * <p>
   * 匹配logger name，拆分类名后和已创建（包括配置的）的Logger进行匹配
   * <p>
   * 比如当前name为com.aaa.bbb.ccc.XXService，那么name为com/com.aaa/com.aaa.bbb/com.aaa.bbb.ccc
   * <p>
   * 的logger都可以作为parent logger，不过这里需要顺序拆分，优先匹配“最近的”
   * <p>
   * 在这个例子里就会优先匹配com.aaa.bbb.ccc这个logger，作为自己的parent
   * <p>
   * 如果没有任何一个logger匹配，那么就使用root logger作为自己的parent
   *
   * @param name Logger name
   */
  @Override
  public Logger newLogger(String name) {
    CustomLogger logger = new CustomLogger();
    logger.setName(name);
    Logger parent = null;
    //拆分包名，向上查找parent logger
    for (int i = name.lastIndexOf("."); i >= 0; i = name.lastIndexOf(".", i - 1)) {
      String parentName = name.substring(0, i);
      parent = loggerContext.getLoggerCache().get(parentName);
      if (parent != null) {
        break;
      }
    }
    if (parent == null) {
      parent = loggerContext.getRoot();
    }
    logger.setAppender(new ConsoleAppender());
    // logger.setAppender(event -> {
    //   System.out.println(event.getLoggerName());
    //   System.out.println(event.getLevel());
    //   System.out.println(event.getMessage());
    //   System.out.println("----------------");
    //   System.out.println(event);
    // });
    logger.setParent(parent);
    logger.setLoggerContext(loggerContext);
    return logger;
  }
}
