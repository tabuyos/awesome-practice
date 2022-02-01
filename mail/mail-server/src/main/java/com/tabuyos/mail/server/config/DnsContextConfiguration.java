/*
 * Copyright (c) 2018-2022 Tabuyos All Right Reserved.
 */
package com.tabuyos.mail.server.config;

import org.apache.james.dnsservice.dnsjava.DNSJavaService;
import org.apache.james.metrics.api.MetricFactory;
import org.springframework.context.annotation.Bean;

/**
 * DnsContextConfiguration
 *
 * @author tabuyos
 * @since 2022/1/29
 */
public class DnsContextConfiguration {

  @Bean
  public DNSJavaService dnsJavaService(MetricFactory metricFactory) {
    return new DNSJavaService(metricFactory);
  }
}
