/*
 * Copyright (c) 2018-2021 Tabuyos All Right Reserved.
 */
package com.tabuyos.authserver.application;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * AuthenticationServerApplication
 *
 * @author tabuyos
 * @since 2021/12/10
 */
@SpringBootApplication(scanBasePackages = {"com.tabuyos.authserver"})
public class AuthenticationServerApplication {

  private static final Logger logger = LoggerFactory.getLogger(AuthenticationServerApplication.class);

  public static void main(String[] args) {
    SpringApplication.run(AuthenticationServerApplication.class, args);
    logger.info("Authentication server is started!");
  }
}
