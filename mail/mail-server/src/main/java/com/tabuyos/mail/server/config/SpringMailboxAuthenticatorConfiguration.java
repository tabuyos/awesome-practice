/*
 * Copyright (c) 2018-2022 Tabuyos All Right Reserved.
 */
package com.tabuyos.mail.server.config;

import org.apache.james.adapter.mailbox.UserRepositoryAuthenticator;
import org.apache.james.mailbox.Authenticator;
import org.apache.james.user.api.UsersRepository;
import org.springframework.context.annotation.Bean;

/**
 * SpringMailboxAuthenticatorConfiguration
 *
 * @author tabuyos
 * @since 2022/1/29
 */
public class SpringMailboxAuthenticatorConfiguration {

  @Bean
  public Authenticator authenticator(UsersRepository repos) {
    return new UserRepositoryAuthenticator(repos);
  }
}
