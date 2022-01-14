/*
 * Copyright (c) 2018-2022 Tabuyos All Right Reserved.
 */
package com.tabuyos.kafkap2p.sendtype;

import com.tabuyos.kafkap2p.config.KafkaConfig;
import com.tabuyos.kafkap2p.constant.KafkaConstant;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.TopicPartition;

import java.time.Duration;
import java.util.Collections;
import java.util.concurrent.TimeUnit;

/**
 * SendTypeKafkaConsumer
 *
 * @author tabuyos
 * @since 2022/1/12
 */
public class SendTypeKafkaConsumer {


  public static void main(String[] args) {
    // 实际的消费者
    try (KafkaConsumer<String, String> consumer = new KafkaConsumer<>(KafkaConfig.consumerConfig(KafkaConstant.P2P_GROUP_ID))) {
      // consumer.subscribe(Collections.singletonList(KafkaConstant.HELLO_TOPIC));
      consumer.assign(Collections.singletonList(new TopicPartition(KafkaConstant.HELLO_TOPIC, 2)));
      while (true) {
        ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(500));
        for (ConsumerRecord<String, String> record : records) {
          System.out.printf(
            "topics: %s, partition: %d, offset: %d, key: %s, value: %s%n",
            record.topic(), record.partition(), record.offset(), record.key(), record.value());
        }
      }
    }
  }
}
