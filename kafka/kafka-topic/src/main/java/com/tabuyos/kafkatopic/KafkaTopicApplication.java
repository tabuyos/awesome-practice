/*
 * Copyright (c) 2018-2022 Tabuyos All Right Reserved.
 */
package com.tabuyos.kafkatopic;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * KafkaTopicApplication
 *
 * @author tabuyos
 * @since 2022/1/12
 */
public class KafkaTopicApplication {

  public static void main(String[] args) {
    List<Consumer<Integer>> collect = IntStream.range(0, 100)
                                               .mapToObj(KafkaTopicApplication::consumer)
                                               .collect(Collectors.toList());
    collect.forEach(consumer -> consumer.accept(1));
  }

  public static Consumer<Integer> consumer(int index) {
    System.out.println("current index: " + index);
    return integer -> {
      try {
        System.out.println(index);
        TimeUnit.SECONDS.sleep(1);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    };
  }
}
