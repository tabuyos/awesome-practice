/*
 * copyright(c) 2018-2021 tabuyos all right reserved.
 */
package com.tabuyos.oauth2.application;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Oauth2Application
 *
 * @author tabuyos
 * @since 2021/12/10
 */
@SpringBootApplication(scanBasePackages = {"com.tabuyos.oauth2"})
public class Oauth2Application {

  private static final Logger logger = LoggerFactory.getLogger(Oauth2Application.class);

  public static void main(String[] args) {
    SpringApplication.run(Oauth2Application.class, args);
    logger.info("oauth 2.1 application is started");
  }
}
