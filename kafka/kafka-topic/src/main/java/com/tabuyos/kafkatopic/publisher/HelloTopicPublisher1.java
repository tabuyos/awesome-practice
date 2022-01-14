/*
 * Copyright (c) 2018-2022 Tabuyos All Right Reserved.
 */
package com.tabuyos.kafkatopic.publisher;

import com.tabuyos.kafkatopic.config.KafkaConfig;
import com.tabuyos.kafkatopic.constant.KafkaConstant;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.concurrent.TimeUnit;

/**
 * HelloTopicPublisher
 *
 * @author tabuyos
 * @since 2022/1/14
 */
public class HelloTopicPublisher1 {

  public static void main(String[] args) {
    new Thread(() -> runProducer(0)).start();
    new Thread(() -> runProducer(1)).start();
    new Thread(() -> runProducer(2)).start();
  }

  public static void runProducer(int index) {
    // 实际的生产者
    try (KafkaProducer<String, String> producer = new KafkaProducer<>(KafkaConfig.producerConfig())) {
      // 构建我们的消息本身
      ProducerRecord<String, String> record;
      record = new ProducerRecord<>(KafkaConstant.HELLO_TOPIC, "teacher-" + index, "tabuyos" + index);
      producer.send(record);
      producer.send(record);
      if (index % 2 == 1) {
        TimeUnit.SECONDS.sleep(3);
      }
      producer.send(record);
      producer.send(record);
      System.out.println("message is sent.");
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
