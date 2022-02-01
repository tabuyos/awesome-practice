/*
 * Copyright (c) 2018-2022 Tabuyos All Right Reserved.
 */
package com.tabuyos.mail.server.config;

import org.jboss.netty.util.HashedWheelTimer;
import org.apache.james.dnsservice.api.DNSService;
import org.apache.james.filesystem.api.FileSystem;
import org.apache.james.metrics.api.MetricFactory;
import org.apache.james.protocols.lib.handler.ProtocolHandlerLoader;
import org.apache.james.smtpserver.netty.SMTPServerFactory;
import org.springframework.context.annotation.Bean;

/**
 * SmtpserverContextConfiguration
 *
 * @author tabuyos
 * @since 2022/1/29
 */
public class SmtpServerContextConfiguration {

  @Bean
  public SMTPServerFactory smtpServerFactory(DNSService dnsService,
                                             ProtocolHandlerLoader handlerLoader,
                                             FileSystem fileSystem,
                                             MetricFactory metricFactory,
                                             HashedWheelTimer timer) {
    return new SMTPServerFactory(dnsService, handlerLoader, fileSystem, metricFactory, timer);
  }
}
