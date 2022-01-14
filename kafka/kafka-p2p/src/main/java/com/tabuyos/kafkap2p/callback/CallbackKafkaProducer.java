/*
 * Copyright (c) 2018-2022 Tabuyos All Right Reserved.
 */
package com.tabuyos.kafkap2p.callback;

import com.tabuyos.kafkap2p.config.KafkaConfig;
import com.tabuyos.kafkap2p.constant.KafkaConstant;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

/**
 * CallbackKafkaProducer
 *
 * @author tabuyos
 * @since 2022/1/12
 */
public class CallbackKafkaProducer {

  public static void main(String[] args) {
    // 实际的生产者
    try (KafkaProducer<String, String> producer = new KafkaProducer<>(KafkaConfig.producerConfig())) {
      // 构建我们的消息本身
      ProducerRecord<String, String> record;
      record = new ProducerRecord<>(KafkaConstant.HELLO_TOPIC, "teacher-0", "tabuyos");
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
      System.out.println("message is sent.");
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
