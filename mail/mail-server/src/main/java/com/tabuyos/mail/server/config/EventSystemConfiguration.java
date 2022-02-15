/*
 * copyright(c) 2018-2022 tabuyos all right reserved.
 */
package com.tabuyos.mail.server.config;

import org.apache.james.events.EventDeadLetters;
import org.apache.james.events.InVMEventBus;
import org.apache.james.events.MemoryEventDeadLetters;
import org.apache.james.events.RetryBackoffConfiguration;
import org.apache.james.events.delivery.EventDelivery;
import org.apache.james.events.delivery.InVmEventDelivery;
import org.apache.james.metrics.api.MetricFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;

/**
 * EventSystemConfiguration
 *
 * @author tabuyos
 * @since 2022/2/3
 */
public class EventSystemConfiguration {

  @Bean
  @Lazy
  public InVMEventBus inVMEventBus(EventDelivery eventDelivery, RetryBackoffConfiguration retryBackoff, EventDeadLetters eventDeadLetters) {
    return new InVMEventBus(eventDelivery, retryBackoff, eventDeadLetters);
  }

  @Bean
  @Lazy
  public InVmEventDelivery inVmEventDelivery(MetricFactory metricFactory) {
    return new InVmEventDelivery(metricFactory);
  }

  @Bean
  @Lazy
  public MemoryEventDeadLetters memoryEventDeadLetters() {
    return new MemoryEventDeadLetters();
  }
}
