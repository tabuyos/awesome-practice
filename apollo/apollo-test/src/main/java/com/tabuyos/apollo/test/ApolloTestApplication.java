/*
 * Copyright (c) 2018-2022 Tabuyos All Right Reserved.
 */
package com.tabuyos.apollo.test;

import com.ctrip.framework.apollo.spring.annotation.EnableApolloConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * ApolloTestApplication
 *
 * @author tabuyos
 * @since 2022/1/5
 */
@SpringBootApplication
@EnableApolloConfig
public class ApolloTestApplication {

  public static void main(String[] args) {
    SpringApplication.run(ApolloTestApplication.class, args);
  }
}
