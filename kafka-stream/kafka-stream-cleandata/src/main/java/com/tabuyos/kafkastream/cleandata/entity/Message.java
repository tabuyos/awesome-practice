/*
 * Copyright (c) 2018-2022 Tabuyos All Right Reserved.
 */
package com.tabuyos.kafkastream.cleandata.entity;

import java.util.Date;
import java.util.StringJoiner;

/**
 * Message
 *
 * @author tabuyos
 * @since 2022/1/17
 */
public class Message {

  private Long id;
  private String message;
  private Date sendTime;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public Date getSendTime() {
    return sendTime;
  }

  public void setSendTime(Date sendTime) {
    this.sendTime = sendTime;
  }

  @Override
  public String toString() {
    return new StringJoiner(", ", Message.class.getSimpleName() + "(", ")")
      .add("id=" + id)
      .add("message='" + message + "'")
      .add("sendTime=" + sendTime)
      .toString();
  }
}
