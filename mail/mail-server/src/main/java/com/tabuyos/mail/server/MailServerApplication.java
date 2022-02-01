/*
 * Copyright (c) 2018-2022 Tabuyos All Right Reserved.
 */
package com.tabuyos.mail.server;

import org.apache.james.container.spring.context.JamesServerApplicationContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

import java.util.Calendar;

/**
 * MailServerApplication
 *
 * @author tabuyos
 * @since 2022/1/29
 */
@SpringBootApplication
public class MailServerApplication implements CommandLineRunner {

  private static final Logger logger = LoggerFactory.getLogger(MailServerApplication.class);
  private JamesServerApplicationContext context;

  public static void main(String[] args) {
    // SpringApplication.run(MailServerApplication.class, args);
    new SpringApplicationBuilder(MailServerApplication.class)
      .web(WebApplicationType.NONE)
      .run(args);
    logger.info("started!");
  }

  @Override
  public void run(String... args) throws Exception {
    long start = Calendar.getInstance().getTimeInMillis();

    context = new JamesServerApplicationContext(new String[]{"META-INF/org/apache/james/spring-server.xml"});
    context.registerShutdownHook();
    context.start();

    long end = Calendar.getInstance().getTimeInMillis();

    logger.info("Tabuyos Server is successfully started in {} milliseconds.", end - start);
  }
}
