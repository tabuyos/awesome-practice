/*
 * Copyright (c) 2018-2022 Tabuyos All Right Reserved.
 */
package com.tabuyos.kafkastream.cleandata.sender;

import com.tabuyos.kafkastream.cleandata.processor.LogProcessor;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.Topology;

import java.util.Properties;

/**
 * KafkaStreamSender
 *
 * @author tabuyos
 * @since 2022/1/17
 */
public class KafkaStreamSender {

  public static void main(String[] args) {

    // 定义输入的topic
    String from = "first";
    // 定义输出的topic
    String to = "second";

    // 设置参数
    Properties settings = new Properties();
    settings.put(StreamsConfig.APPLICATION_ID_CONFIG, "logFilter");
    settings.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
    settings.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.ByteArray().getClass());
    settings.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.ByteArray().getClass());

    StreamsConfig config = new StreamsConfig(settings);

    // 构建拓扑
    Topology builder = new Topology();
    // 过时的
    // TopologyBuilder builder = new TopologyBuilder();

    // 具体分析处理
    builder.addSource("SOURCE", from)
           .addProcessor("PROCESS", LogProcessor::new, "SOURCE")
           .addSink("SINK", to, "PROCESS");

    // 创建kafka stream
    KafkaStreams streams = new KafkaStreams(builder, config);
    streams.start();
  }
}
