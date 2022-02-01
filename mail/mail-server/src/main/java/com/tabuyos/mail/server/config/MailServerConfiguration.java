/*
 * Copyright (c) 2018-2022 Tabuyos All Right Reserved.
 */
package com.tabuyos.mail.server.config;

import org.jboss.netty.util.HashedWheelTimer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.CommonAnnotationBeanPostProcessor;
import org.springframework.context.annotation.Import;
import org.springframework.orm.jpa.support.PersistenceAnnotationBeanPostProcessor;

/**
 * MailServerConfiguration
 *
 * @author tabuyos
 * @since 2022/1/29
 */
@Import({
  LoadersContextConfiguration.class,
  SpringMailboxAuthenticatorConfiguration.class,
  SpringMailboxAuthorizatorConfiguration.class,
  ActivemqQueueContextConfiguration.class,
  CamelConfiguration.class,
  DnsContextConfiguration.class,
  FetchMailConfiguration.class,
  SmtpServerContextConfiguration.class,
  LmtpServerContextConfiguration.class,
  Pop3ServerContextConfiguration.class,
  ImapServerContextConfiguration.class,
  ManageSieveServerContextConfiguration.class,
})
public class MailServerConfiguration {

  @Bean
  public CommonAnnotationBeanPostProcessor commonAnnotationBeanPostProcessor() {
    CommonAnnotationBeanPostProcessor processor = new CommonAnnotationBeanPostProcessor();
    processor.setOrder(3);
    return processor;
  }

  @Bean
  public PersistenceAnnotationBeanPostProcessor persistenceAnnotationBeanPostProcessor() {
    return new PersistenceAnnotationBeanPostProcessor();
  }

  @Bean
  public HashedWheelTimer hashedWheelTimer() {
    return new HashedWheelTimer();
  }
}
