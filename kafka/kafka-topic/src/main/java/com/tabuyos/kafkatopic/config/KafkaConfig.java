/*
 * Copyright (c) 2018-2022 Tabuyos All Right Reserved.
 */
package com.tabuyos.kafkatopic.config;

import com.tabuyos.kafkatopic.constant.KafkaConstant;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;

/**
 * KafkaConfig
 *
 * @author tabuyos
 * @since 2022/1/11
 */
public class KafkaConfig {

  /**
   * 生产者配置
   *
   * @return config
   */
  public static Properties producerConfig() {
    Properties properties = new Properties();
    properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, KafkaConstant.HOST);
    properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
    properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
    properties.put(ProducerConfig.BUFFER_MEMORY_CONFIG, 32 * 100 * 1024 * 1024L);
    return properties;
  }

  /**
   * 消费者配置
   *
   * @return config
   */
  public static Properties consumerConfig() {
    return consumerConfig(KafkaConstant.TOPIC_1_GROUP_ID);
  }

  /**
   * 消费者配置
   *
   * @param groupId group id
   * @return config
   */
  public static Properties consumerConfig(String groupId) {
    return consumerConfig(groupId, true);
  }

  /**
   * 消费者配置
   *
   * @param groupId    group id
   * @param autoCommit auto commit if true
   * @return config
   */
  public static Properties consumerConfig(String groupId, boolean autoCommit) {
    Properties properties = new Properties();
    properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, KafkaConstant.HOST);
    properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
    properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
    properties.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
    properties.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, autoCommit);
    return properties;
  }
}
