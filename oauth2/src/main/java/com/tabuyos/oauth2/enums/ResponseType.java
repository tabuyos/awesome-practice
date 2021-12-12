/*
 * copyright(c) 2018-2021 tabuyos all right reserved.
 */
package com.tabuyos.oauth2.enums;

/**
 * ResponseType
 *
 * @author tabuyos
 * @since 2021/12/12
 */
public enum ResponseType {
  /**
   * Authorization Code Grant
   */
  CODE("code"),
  /**
   * Implicit Grant
   */
  TOKEN("token");

  /**
   * response type for oauth 2.1
   */
  public final String type;

  ResponseType(String type) {
    this.type = type;
  }
}
