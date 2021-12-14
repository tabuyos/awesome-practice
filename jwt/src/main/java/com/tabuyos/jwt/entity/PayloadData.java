/*
 * Copyright (c) 2018-2021 Tabuyos All Right Reserved.
 */
package com.tabuyos.jwt.entity;

import java.util.List;
import java.util.StringJoiner;

/**
 * PayloadData
 *
 * @author tabuyos
 * @since 2021/12/14
 */
public class PayloadData {
  /** 主题 */
  private String subject;
  /** 签发人 */
  private Long issuer;
  /** 签发时间 */
  private Long issueAt;
  /** 过期时间 */
  private Long expire;
  /** JWT的ID */
  private String jwtId;
  /** 用户名称 */
  private String username;
  /** 用户拥有的权限 */
  private List<String> authorities;

  public String getSubject() {
    return subject;
  }

  public void setSubject(String subject) {
    this.subject = subject;
  }

  public Long getIssuer() {
    return issuer;
  }

  public void setIssuer(Long issuer) {
    this.issuer = issuer;
  }

  public Long getIssueAt() {
    return issueAt;
  }

  public void setIssueAt(Long issueAt) {
    this.issueAt = issueAt;
  }

  public Long getExpire() {
    return expire;
  }

  public void setExpire(Long expire) {
    this.expire = expire;
  }

  public String getJwtId() {
    return jwtId;
  }

  public void setJwtId(String jwtId) {
    this.jwtId = jwtId;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public List<String> getAuthorities() {
    return authorities;
  }

  public void setAuthorities(List<String> authorities) {
    this.authorities = authorities;
  }

  @Override
  public String toString() {
    return new StringJoiner(", ", PayloadData.class.getSimpleName() + "(", ")")
      .add("subject='" + subject + "'")
      .add("issuer=" + issuer)
      .add("issueAt=" + issueAt)
      .add("expire=" + expire)
      .add("jwtId='" + jwtId + "'")
      .add("username='" + username + "'")
      .add("authorities=" + authorities)
      .toString();
  }


  public static PayloadDataBuilder builder() {
    return new PayloadDataBuilder();
  }

  public static final class PayloadDataBuilder {

    private final PayloadData payloadData;

    private PayloadDataBuilder() {
      payloadData = new PayloadData();
    }

    public PayloadDataBuilder subject(String subject) {
      payloadData.setSubject(subject);
      return this;
    }

    public PayloadDataBuilder issuer(Long issuer) {
      payloadData.setIssuer(issuer);
      return this;
    }

    public PayloadDataBuilder issueAt(Long issueAt) {
      payloadData.setIssueAt(issueAt);
      return this;
    }

    public PayloadDataBuilder expire(Long expire) {
      payloadData.setExpire(expire);
      return this;
    }

    public PayloadDataBuilder jwtId(String jwtId) {
      payloadData.setJwtId(jwtId);
      return this;
    }

    public PayloadDataBuilder username(String username) {
      payloadData.setUsername(username);
      return this;
    }

    public PayloadDataBuilder authorities(List<String> authorities) {
      payloadData.setAuthorities(authorities);
      return this;
    }

    public PayloadData build() {
      return payloadData;
    }
  }
}
