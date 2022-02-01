/*
 * Copyright (c) 2018-2022 Tabuyos All Right Reserved.
 */
package com.tabuyos.mail.server.config;

import org.apache.james.container.spring.bean.factory.mailetcontainer.MailetLoaderBeanFactory;
import org.apache.james.container.spring.bean.factory.mailetcontainer.MatcherLoaderBeanFactory;
import org.apache.james.container.spring.bean.factory.mailrepositorystore.MailRepositoryStoreBeanFactory;
import org.apache.james.container.spring.bean.factory.protocols.ProtocolHandlerLoaderBeanFactory;
import org.apache.james.container.spring.bean.factorypostprocessor.ConfigurationBeanFactoryPostProcessor;
import org.apache.james.container.spring.bean.factorypostprocessor.FileSystemBeanFactoryPostProcessor;
import org.apache.james.container.spring.filesystem.ResourceLoaderFileSystem;
import org.apache.james.container.spring.lifecycle.ConfigurableBeanPostProcessor;
import org.apache.james.container.spring.lifecycle.ConfigurationProvider;
import org.apache.james.container.spring.lifecycle.ConfigurationProviderImpl;
import org.apache.james.managesieve.jsieve.Parser;
import org.springframework.context.annotation.Bean;

import java.util.HashMap;
import java.util.Map;

/**
 * LoadersContext
 *
 * @author tabuyos
 * @since 2022/1/29
 */
public class LoadersContextConfiguration {

  @Bean
  public ConfigurationBeanFactoryPostProcessor configurationBeanFactoryPostProcessor() {
    ConfigurationBeanFactoryPostProcessor processor = new ConfigurationBeanFactoryPostProcessor();
    Map<String, String> map = new HashMap<>(4);
    map.put("usersrepository", null);
    map.put("sieverepository", null);
    map.put("recipientrewritetable", null);
    map.put("domainlist", null);
    processor.setBeans(map);
    return processor;
  }

  @Bean
  public ConfigurableBeanPostProcessor configurableBeanPostProcessor(ConfigurationProvider configurationProvider) {
    ConfigurableBeanPostProcessor processor = new ConfigurableBeanPostProcessor();
    processor.setOrder(1);
    processor.setConfigurationProvider(configurationProvider);
    return processor;
  }

  @Bean
  public ConfigurationProvider configurationProvider() {
    ConfigurationProviderImpl provider = new ConfigurationProviderImpl();
    Map<String, String> configurations = new HashMap<>(3);
    configurations.put("mailprocessor", "mailetcontainer.processors");
    configurations.put("mailspooler", "mailetcontainer.spooler");
    configurations.put("mailetcontext", "mailetcontainer.context");
    provider.setConfigurationMappings(configurations);
    return provider;
  }

  @Bean
  public MailRepositoryStoreBeanFactory mailRepositoryStore() {
    return new MailRepositoryStoreBeanFactory();
  }

  @Bean
  public MailetLoaderBeanFactory mailetLoader() {
    return new MailetLoaderBeanFactory();
  }

  @Bean
  public MatcherLoaderBeanFactory matcherLoader() {
    return new MatcherLoaderBeanFactory();
  }

  @Bean
  public ProtocolHandlerLoaderBeanFactory protocolHandlerLoader() {
    return new ProtocolHandlerLoaderBeanFactory();
  }

  @Bean
  public FileSystemBeanFactoryPostProcessor fileSystemBeanFactoryPostProcessor(ResourceLoaderFileSystem fileSystem) {
    FileSystemBeanFactoryPostProcessor processor = new FileSystemBeanFactoryPostProcessor();
    processor.setFileSystem(fileSystem);
    return processor;
  }

  @Bean
  public ResourceLoaderFileSystem resourceLoaderFileSystem() {
    return new ResourceLoaderFileSystem();
  }

  @Bean
  public Parser parser() throws Exception {
    return new Parser();
  }
}
