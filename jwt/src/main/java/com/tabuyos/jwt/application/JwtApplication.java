/*
 * Copyright (c) 2018-2021 Tabuyos All Right Reserved.
 */
package com.tabuyos.jwt.application;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * JwtApplication
 *
 * @author tabuyos
 * @since 2021/12/14
 */
@SpringBootApplication(scanBasePackages = {"com.tabuyos.jwt"})
public class JwtApplication {

  private static final Logger logger = LoggerFactory.getLogger(JwtApplication.class);

  public static void main(String[] args) {
    SpringApplication.run(JwtApplication.class, args);
    logger.info("Jwt Application is Started!");
  }
}
