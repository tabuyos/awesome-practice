/*
 * Copyright (c) 2018-2021 Tabuyos All Right Reserved.
 */
package com.tabuyos.jwt.exception;

/**
 * JwtInvalidException
 *
 * @author tabuyos
 * @since 2021/12/14
 */
public class JwtInvalidException extends RuntimeException {

  public JwtInvalidException(String message) {
    super(message);
  }
}
