/*
 * Copyright (c) 2018-2022 Tabuyos All Right Reserved.
 */
package com.tabuyos.kafkastream.cleandata.processor;


import org.apache.kafka.streams.processor.api.Processor;
import org.apache.kafka.streams.processor.api.ProcessorContext;
import org.apache.kafka.streams.processor.api.Record;

import java.nio.charset.StandardCharsets;

/**
 * LogProcessor
 *
 * @author tabuyos
 * @since 2022/1/17
 */
public class LogProcessor implements Processor<byte[], byte[], byte[], byte[]> {

  private ProcessorContext<byte[], byte[]> context;

  @Override
  public void process(Record<byte[], byte[]> record) {
    byte[] value = record.value();
    byte[] key = record.key();
    String input = new String(value);
    String flag = ">>>";
    System.out.println(input);
    // 如果包含“>>>”则替换为<<<
    if (input.contains(flag)) {
      input = input.replaceAll(">>>", "<<<");
      // 输出到下一个topic
      Record<byte[], byte[]> result = record.withKey(key).withValue(input.getBytes(StandardCharsets.UTF_8));
      context.forward(result);
    } else {
      context.forward(record);
    }
  }

  @Override
  public void init(ProcessorContext<byte[], byte[]> context) {
    this.context = context;
  }

  @Override
  public void close() {
    Processor.super.close();
  }
}
