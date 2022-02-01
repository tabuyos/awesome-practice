/*
 * Copyright (c) 2018-2022 Tabuyos All Right Reserved.
 */
package com.tabuyos.mail.server.config;

import org.apache.james.events.EventBus;
import org.apache.james.filesystem.api.FileSystem;
import org.apache.james.imap.api.process.ImapProcessor;
import org.apache.james.imap.decode.ImapDecoder;
import org.apache.james.imap.decode.ImapDecoderFactory;
import org.apache.james.imap.encode.ImapEncoder;
import org.apache.james.imap.encode.ImapEncoderFactory;
import org.apache.james.imap.encode.main.DefaultImapEncoderFactory;
import org.apache.james.imap.main.DefaultImapDecoderFactory;
import org.apache.james.imap.processor.main.DefaultImapProcessorFactory;
import org.apache.james.imapserver.netty.IMAPServerFactory;
import org.apache.james.mailbox.MailboxManager;
import org.apache.james.mailbox.SubscriptionManager;
import org.apache.james.mailbox.quota.QuotaManager;
import org.apache.james.mailbox.quota.QuotaRootResolver;
import org.apache.james.metrics.api.MetricFactory;
import org.apache.james.metrics.logger.DefaultMetricFactory;
import org.apache.james.protocols.lib.handler.ProtocolHandlerLoader;
import org.jboss.netty.util.HashedWheelTimer;
import org.springframework.context.annotation.Bean;

/**
 * ImapServerContextConfiguration
 *
 * @author tabuyos
 * @since 2022/1/29
 */
public class ImapServerContextConfiguration {

  @Bean
  public IMAPServerFactory imapServerFactory(ProtocolHandlerLoader handlerLoader,
                                             FileSystem fileSystem,
                                             ImapDecoder imapDecoder,
                                             ImapEncoder imapEncoder,
                                             ImapProcessor imapProcessor,
                                             MetricFactory metricFactory,
                                             HashedWheelTimer hashedWheelTimer) {
    return new IMAPServerFactory(fileSystem, imapDecoder, imapEncoder, imapProcessor, metricFactory, hashedWheelTimer);
  }

  @Bean
  public ImapProcessor defaultImapProcessorFactory(MailboxManager mailboxManager,
                                                   EventBus eventBus,
                                                   SubscriptionManager subscriptionManager,
                                                   QuotaManager quotaManager,
                                                   QuotaRootResolver quotaRootResolver,
                                                   MetricFactory metricFactory) {
    return DefaultImapProcessorFactory
      .createXListSupportingProcessor(mailboxManager,
                                      eventBus,
                                      subscriptionManager,
                                      null,
                                      quotaManager,
                                      quotaRootResolver,
                                      metricFactory);
  }

  @Bean
  public ImapDecoderFactory imapDecoderFactory() {
    return new DefaultImapDecoderFactory();
  }

  @Bean
  public ImapDecoder imapDecoder(ImapDecoderFactory imapDecoderFactory) {
    return imapDecoderFactory.buildImapDecoder();
  }

  @Bean
  public ImapEncoderFactory imapEncoderFactory() {
    return new DefaultImapEncoderFactory();
  }

  @Bean
  public ImapEncoder imapDecoder(ImapEncoderFactory imapEncoderFactory) {
    return imapEncoderFactory.buildImapEncoder();
  }

  @Bean
  public MetricFactory metricFactory() {
    return new DefaultMetricFactory();
  }
}
