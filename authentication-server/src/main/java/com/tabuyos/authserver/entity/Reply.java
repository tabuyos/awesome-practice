/*
 * Copyright (c) 2018-2021 Tabuyos All Right Reserved.
 */
package com.tabuyos.authserver.entity;

import java.util.Map;
import java.util.StringJoiner;

/**
 * Reply
 *
 * @author tabuyos
 * @since 2021/12/10
 */
public class Reply {

  private Integer code;
  private Boolean state;
  private String message;
  private Map<String, String> data;

  public Integer getCode() {
    return code;
  }

  public void setCode(Integer code) {
    this.code = code;
  }

  public Boolean getState() {
    return state;
  }

  public void setState(Boolean state) {
    this.state = state;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public Map<String, String> getData() {
    return data;
  }

  public void setData(Map<String, String> data) {
    this.data = data;
  }

  @Override
  public String toString() {
    return new StringJoiner(", ", Reply.class.getSimpleName() + "(", ")")
      .add("code=" + code)
      .add("state=" + state)
      .add("message='" + message + "'")
      .add("data=" + data)
      .toString();
  }
}
