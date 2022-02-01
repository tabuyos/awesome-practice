/*
 * Copyright (c) 2018-2022 Tabuyos All Right Reserved.
 */
package com.tabuyos.mail.server.config;

import org.apache.james.filesystem.api.FileSystem;
import org.apache.james.lmtpserver.netty.LMTPServerFactory;
import org.apache.james.metrics.api.MetricFactory;
import org.apache.james.protocols.lib.handler.ProtocolHandlerLoader;
import org.jboss.netty.util.HashedWheelTimer;
import org.springframework.context.annotation.Bean;

/**
 * LmtpServerContextConfiguration
 *
 * @author tabuyos
 * @since 2022/1/29
 */
public class LmtpServerContextConfiguration {

  @Bean
  public LMTPServerFactory lmtpServerFactory(ProtocolHandlerLoader handlerLoader, FileSystem fileSystem,
                                             MetricFactory metricFactory, HashedWheelTimer timer) {
    return new LMTPServerFactory(handlerLoader, fileSystem, metricFactory, timer);
  }
}
