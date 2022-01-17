/*
 * Copyright (c) 2018-2022 Tabuyos All Right Reserved.
 */
package com.tabuyos.kafkastream.cleandata.receiver;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * KafkaReceiver
 *
 * @author tabuyos
 * @since 2022/1/17
 */
@Component
public class KafkaReceiver {

  private static final Logger log = LoggerFactory.getLogger(KafkaReceiver.class);

  @KafkaListener(topics = {"first"})
  public void listen(ConsumerRecord<?, ?> record) {
    Optional<?> kafkaMessage = Optional.ofNullable(record.value());
    if (kafkaMessage.isPresent()) {
      Object message = kafkaMessage.get();
      log.info("record =" + record);
      log.info("message =" + message);
    }
  }
}
