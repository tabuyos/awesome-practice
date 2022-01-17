/*
 * Copyright (c) 2018-2022 Tabuyos All Right Reserved.
 */
package com.tabuyos.kafkastream.cleandata;

import com.tabuyos.kafkastream.cleandata.sender.KafkaSender;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

/**
 * KafkaStreamApplication
 *
 * @author tabuyos
 * @since 2022/1/17
 */
@SpringBootApplication
public class KafkaStreamApplication {

  public static void main(String[] args) {
    ConfigurableApplicationContext context = SpringApplication.run(KafkaStreamApplication.class, args);
    KafkaSender sender = context.getBean(KafkaSender.class);
    IntStream.range(0, 6).forEach(el -> {
      System.out.println(el);
      sender.send();
      try {
        TimeUnit.SECONDS.sleep(3);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    });
  }
}
