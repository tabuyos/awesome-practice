/*
 * Copyright (c) 2018-2022 Tabuyos All Right Reserved.
 */
package com.tabuyos.openapi.quickstart.vo;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.StringJoiner;

/**
 * UserVO
 *
 * @author tabuyos
 * @since 2022/2/15
 */
@Schema(description = "用户实体")
public class User {

  public User() {
  }

  public User(String userName, String email) {
    this.userName = userName;
    this.email = email;
  }

  @Schema(name = "userName", description = "用户名")
  private String userName;

  @Schema(description = "邮箱")
  private String email;

  public String getUserName() {
    return userName;
  }

  public void setUserName(String userName) {
    this.userName = userName;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  @Override
  public String toString() {
    return new StringJoiner(", ", User.class.getSimpleName() + "(", ")")
      .add("userName='" + userName + "'")
      .add("email='" + email + "'")
      .toString();
  }
}
