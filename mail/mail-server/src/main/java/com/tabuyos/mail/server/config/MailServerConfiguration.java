/*
 * Copyright (c) 2018-2022 Tabuyos All Right Reserved.
 */
package com.tabuyos.mail.server.config;

import org.apache.james.container.spring.bean.factorypostprocessor.IndexerConfigurationBeanFactoryPostProcessor;
import org.apache.james.container.spring.bean.factorypostprocessor.MailboxConfigurationBeanFactoryPostProcessor;
import org.apache.james.container.spring.bean.factorypostprocessor.QuotaBeanFactoryPostProcessor;
import org.apache.james.container.spring.mailbox.MaxQuotaConfigurationReader;
import org.apache.james.container.spring.mailbox.SpringResolver;
import org.apache.james.mailbox.copier.MailboxCopier;
import org.apache.james.mailbox.quota.MaxQuotaManager;
import org.apache.james.mailbox.quota.QuotaRootResolver;
import org.apache.james.mailbox.store.JVMMailboxPathLocker;
import org.apache.james.mailbox.store.mail.model.impl.MessageParser;
import org.apache.james.mailbox.tools.copier.MailboxCopierImpl;
import org.jboss.netty.util.HashedWheelTimer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.CommonAnnotationBeanPostProcessor;
import org.springframework.context.annotation.Import;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.io.ClassPathResource;
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
  SpringMailboxConfiguration.class,
  QuotaConfiguration.class,
  EventSystemConfiguration.class,
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

  @Bean
  public IndexerConfigurationBeanFactoryPostProcessor indexerConfigurationBeanFactoryPostProcessor() {
    return new IndexerConfigurationBeanFactoryPostProcessor();
  }

  @Bean
  public MessageParser messageParser() {
    return new MessageParser();
  }

  @Bean
  public MailboxConfigurationBeanFactoryPostProcessor mailboxConfigurationBeanFactoryPostProcessor() {
    return new MailboxConfigurationBeanFactoryPostProcessor();
  }

  @Bean
  public JVMMailboxPathLocker jvmMailboxPathLocker() {
    return new JVMMailboxPathLocker();
  }

  @Bean
  public QuotaBeanFactoryPostProcessor quotaBeanFactoryPostProcessor() {
    return new QuotaBeanFactoryPostProcessor();
  }

  @Bean
  public MaxQuotaConfigurationReader maxQuotaConfigurationReader(MaxQuotaManager maxQuotaManager, QuotaRootResolver quotaRootResolver) {
    return new MaxQuotaConfigurationReader(maxQuotaManager, quotaRootResolver);
  }

  @Bean
  public MailboxCopier mailboxCopier() {
    return new MailboxCopierImpl();
  }

  @Bean
  public SpringResolver springResolver() {
    return new SpringResolver();
  }

  @Bean
  public PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
    PropertySourcesPlaceholderConfigurer configurer = new PropertySourcesPlaceholderConfigurer();
    configurer.setIgnoreUnresolvablePlaceholders(true);
    configurer.setLocation(new ClassPathResource("tabuyos-database.properties"));
    return configurer;
  }
}
