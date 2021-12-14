/*
 * Copyright (c) 2018-2021 Tabuyos All Right Reserved.
 */
package com.tabuyos.jwt.exception;

/**
 * JwtExpiredException
 *
 * @author tabuyos
 * @since 2021/12/14
 */
public class JwtExpiredException extends RuntimeException {

  public JwtExpiredException(String message) {
    super(message);
  }
}
