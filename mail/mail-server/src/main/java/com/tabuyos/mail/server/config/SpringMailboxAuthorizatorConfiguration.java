/*
 * Copyright (c) 2018-2022 Tabuyos All Right Reserved.
 */
package com.tabuyos.mail.server.config;

import org.apache.james.adapter.mailbox.UserRepositoryAuthorizator;
import org.apache.james.mailbox.Authorizator;
import org.apache.james.user.api.UsersRepository;
import org.springframework.context.annotation.Bean;

/**
 * SpringMailboxAuthorizatorConfiguration
 *
 * @author tabuyos
 * @since 2022/1/29
 */
public class SpringMailboxAuthorizatorConfiguration {

  @Bean
  public Authorizator authorizator(UsersRepository repos) {
    return new UserRepositoryAuthorizator(repos);
  }
}
