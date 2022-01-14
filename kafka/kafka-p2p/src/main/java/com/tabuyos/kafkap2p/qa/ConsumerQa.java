/*
 * Copyright (c) 2018-2022 Tabuyos All Right Reserved.
 */
package com.tabuyos.kafkap2p.qa;

import com.tabuyos.kafkap2p.config.KafkaConfig;
import com.tabuyos.kafkap2p.constant.KafkaConstant;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

/**
 * Consumer0
 *
 * @author tabuyos
 * @since 2022/1/11
 */
@SuppressWarnings("AlibabaAvoidManuallyCreateThread")
public class ConsumerQa {

  private static KafkaConsumer<String, String> consumer;

  public static void main(String[] args) throws InterruptedException {
    generateCme();
  }

  /**
   * 模拟 ConcurrentModificationException 错误
   * <p>
   * kafka 的 Consumer 是单线程的, 因此, 在多线程中调用时会抛出该异常
   * <p>
   * 关键代码: org.apache.kafka.clients.consumer.KafkaConsumer#acquire()
   * <p>
   * 该方法判断了是否是同一个线程, 如果不是, 则抛出异常
   */
  public static void generateCme() throws InterruptedException {
    consumer = new KafkaConsumer<>(KafkaConfig.consumerConfig(KafkaConstant.P2P_GROUP_ID));
    consumer.subscribe(List.of(KafkaConstant.HELLO_TOPIC));

    new Thread(() -> consumer.poll(Duration.of(100, ChronoUnit.MILLIS))).start();
    new Thread(() -> consumer.poll(Duration.of(100, ChronoUnit.MILLIS))).start();

    TimeUnit.SECONDS.sleep(100);
  }
}
