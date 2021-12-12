/*
 * copyright(c) 2018-2021 tabuyos all right reserved.
 */
package com.tabuyos.oauth2.enums;

/**
 * MimeType
 *
 * @author tabuyos
 * @since 2021/12/11
 */
public enum MimeType {

  /**
   * application/x-www-form-urlencoded
   */
  APPLICATION_X_WWW_FORM_URLENCODED("application/x-www-form-urlencoded"),
  /**
   * application/json
   */
  APPLICATION_JSON("application/json");

  /**
   * mime type for oauth 2.1
   */
  public final String type;

  MimeType(String type) {
    this.type = type;
  }
}
