/*
 * Copyright (c) 2018-2022 Tabuyos All Right Reserved.
 */
package com.tabuyos.kafkap2p.sendtype;

import com.tabuyos.kafkap2p.config.KafkaConfig;
import com.tabuyos.kafkap2p.constant.KafkaConstant;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;

/**
 * SendTypeKafkaProducer
 *
 * @author tabuyos
 * @since 2022/1/12
 */
public class SendTypeKafkaProducer {

  public static void main(String[] args) {
    // 实际的生产者
    try (KafkaProducer<String, String> producer = new KafkaProducer<>(KafkaConfig.producerConfig())) {
      // 构建我们的消息本身
      ProducerRecord<String, String> record;
      record = new ProducerRecord<>(KafkaConstant.HELLO_TOPIC, 2, "teacher-0", "tabuyos");
      // 采用同步非阻塞方式, 可以得知消息是否发送成功
      System.out.println("begin send");
      RecordMetadata metadata = producer.send(record).get();
      System.out.println("end send");
      if (metadata != null) {
        System.out.printf("offset: %d, partition: %d%n", metadata.offset(), metadata.partition());
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
