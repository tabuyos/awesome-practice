/*
 * Copyright (c) 2018-2022 Tabuyos All Right Reserved.
 */
package com.tabuyos.mail.server.config;

import org.apache.james.fetchmail.FetchScheduler;
import org.springframework.context.annotation.Bean;

/**
 * FetchMailConfiguration
 *
 * @author tabuyos
 * @since 2022/1/29
 */
public class FetchMailConfiguration {

  @Bean
  public FetchScheduler fetchScheduler() {
    return new FetchScheduler();
  }
}
