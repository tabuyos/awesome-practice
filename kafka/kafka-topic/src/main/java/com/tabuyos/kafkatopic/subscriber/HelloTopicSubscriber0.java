/*
 * Copyright (c) 2018-2022 Tabuyos All Right Reserved.
 */
package com.tabuyos.kafkatopic.subscriber;

import com.tabuyos.kafkatopic.config.KafkaConfig;
import com.tabuyos.kafkatopic.constant.KafkaConstant;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.time.Duration;
import java.util.Collections;

/**
 * HelloTopicSubscriber
 *
 * @author tabuyos
 * @since 2022/1/14
 */
public class HelloTopicSubscriber0 {

  public static void main(String[] args) {
    // 实际的消费者
    try (KafkaConsumer<String, String> consumer = new KafkaConsumer<>(KafkaConfig.consumerConfig(KafkaConstant.TOPIC_1_GROUP_ID))) {
      consumer.subscribe(Collections.singletonList(KafkaConstant.HELLO_TOPIC));
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
