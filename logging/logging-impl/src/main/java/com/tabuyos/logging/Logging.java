/*
 * Copyright (c) 2018-2022 Tabuyos All Right Reserved.
 */
package com.tabuyos.logging;

import com.tabuyos.logging.factory.LoggerFactory;
import com.tabuyos.logging.logger.Logger;

/**
 * Logging
 *
 * @author tabuyos
 * @since 2022/1/10
 */
public class Logging {

  private static final Logger logger = LoggerFactory.getLogger(Logging.class);

  public static void main(String[] args) {
    say0();
  }

  public static void say0() {
    say1();
  }

  public static void say1() {
    say2();
  }

  public static void say2() {
    logger.trace("tabuyos trace");
    logger.debug("tabuyos debug");
    logger.info("tabuyos info");
    logger.warn("tabuyos warn");
    logger.error("tabuyos error");
  }
}
