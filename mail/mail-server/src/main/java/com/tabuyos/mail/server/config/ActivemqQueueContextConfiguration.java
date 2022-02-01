/*
 * Copyright (c) 2018-2022 Tabuyos All Right Reserved.
 */
package com.tabuyos.mail.server.config;

import org.apache.activemq.store.PersistenceAdapter;
import org.apache.activemq.store.kahadb.KahaDBPersistenceAdapter;
import org.apache.james.filesystem.api.FileSystem;
import org.apache.james.metrics.api.GaugeRegistry;
import org.apache.james.metrics.api.MetricFactory;
import org.apache.james.queue.activemq.ActiveMQMailQueueFactory;
import org.apache.james.queue.activemq.EmbeddedActiveMQ;
import org.apache.james.queue.api.MailQueueItemDecoratorFactory;
import org.apache.james.queue.api.RawMailQueueItemDecoratorFactory;
import org.springframework.context.annotation.Bean;

import javax.inject.Inject;

/**
 * ActivemqQueueContextConfiguration
 *
 * @author tabuyos
 * @since 2022/1/29
 */
public class ActivemqQueueContextConfiguration {

  @Bean
  @Inject
  public ActiveMQMailQueueFactory activeMqMailQueueFactory(EmbeddedActiveMQ embeddedActiveMq,
                                                           MailQueueItemDecoratorFactory mailQueueItemDecoratorFactory,
                                                           MetricFactory metricFactory,
                                                           GaugeRegistry gaugeRegistry) {
    return new ActiveMQMailQueueFactory(embeddedActiveMq, mailQueueItemDecoratorFactory, metricFactory, gaugeRegistry);
  }

  // @Bean
  // public EmbeddedActiveMQ embeddedActiveMq(FileSystem fileSystem, PersistenceAdapter persistenceAdapter) {
  //   return new EmbeddedActiveMQ(fileSystem, persistenceAdapter);
  // }

  @Bean
  public PersistenceAdapter persistenceAdapter() {
    return new KahaDBPersistenceAdapter();
  }

  @Bean
  public MailQueueItemDecoratorFactory mailQueueItemDecoratorFactory() {
    return new RawMailQueueItemDecoratorFactory();
  }
}
