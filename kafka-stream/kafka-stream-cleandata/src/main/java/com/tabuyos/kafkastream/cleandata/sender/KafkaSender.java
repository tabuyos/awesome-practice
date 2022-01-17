/*
 * Copyright (c) 2018-2022 Tabuyos All Right Reserved.
 */
package com.tabuyos.kafkastream.cleandata.sender;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.tabuyos.kafkastream.cleandata.entity.Message;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.UUID;

/**
 * KafkaSender
 *
 * @author tabuyos
 * @since 2022/1/17
 */
@Component
public class KafkaSender {

  private final KafkaTemplate<String, String> kafkaTemplate;
  private final Gson gson = new GsonBuilder().disableHtmlEscaping().create();

  public KafkaSender(KafkaTemplate<String, String> kafkaTemplate) {
    this.kafkaTemplate = kafkaTemplate;
  }

  public void send() {
    Message message = new Message();
    message.setId(System.currentTimeMillis());
    message.setMessage(UUID.randomUUID().toString().concat(">>>").concat(" tabuyos"));
    message.setSendTime(new Date());
    kafkaTemplate.send("first", gson.toJson(message));
  }
}
