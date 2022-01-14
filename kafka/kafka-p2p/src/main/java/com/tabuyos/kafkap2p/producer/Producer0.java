/*
 * Copyright (c) 2018-2022 Tabuyos All Right Reserved.
 */
package com.tabuyos.kafkap2p.producer;

import com.tabuyos.kafkap2p.constant.KafkaConstant;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;

/**
 * Producer0
 *
 * @author tabuyos
 * @since 2022/1/11
 */
public class Producer0 {

  public static void main(String[] args) {
    // 生产者配置
    Properties properties = new Properties();
    properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "127.0.0.1:9092");
    properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
    properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
    // 实际的生产者
    try (KafkaProducer<String, String> producer = new KafkaProducer<>(properties)) {
      // 构建我们的消息本身
      ProducerRecord<String, String> record;
      record = new ProducerRecord<>(KafkaConstant.HELLO_TOPIC, "teacher-0", "tabuyos");
      // 采用同步非阻塞方式
      RecordMetadata metadata = producer.send(record).get();
      if (metadata != null) {
        System.out.printf("offset: %d, partition: %d%n", metadata.offset(), metadata.partition());
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
