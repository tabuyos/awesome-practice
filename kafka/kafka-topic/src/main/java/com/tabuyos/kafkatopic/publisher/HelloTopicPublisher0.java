/*
 * Copyright (c) 2018-2022 Tabuyos All Right Reserved.
 */
package com.tabuyos.kafkatopic.publisher;

import com.tabuyos.kafkatopic.config.KafkaConfig;
import com.tabuyos.kafkatopic.constant.KafkaConstant;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

/**
 * HelloTopicPublisher
 *
 * @author tabuyos
 * @since 2022/1/14
 */
public class HelloTopicPublisher0 {

  public static void main(String[] args) {
    IntStream.range(0, 100).forEach(HelloTopicPublisher0::publisher);
  }

  public static void publisher(int index) {
    // 实际的生产者
    try (KafkaProducer<String, String> producer = new KafkaProducer<>(KafkaConfig.producerConfig())) {
      // 构建我们的消息本身
      ProducerRecord<String, String> record;
      record = new ProducerRecord<>(KafkaConstant.HELLO_TOPIC, "tabuyos-" + index, "tabuyos: " + index);
      // 采用异步方式
      producer.send(
        record,
        (metadata, exception) -> {
          if (exception != null) {
            exception.printStackTrace();
          }
          if (metadata != null) {
            System.out.printf(
              "offset: %d, partition: %d%n", metadata.offset(), metadata.partition());
          }
        });
      System.out.println("message is sent. - " + index);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
