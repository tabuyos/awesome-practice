/*
 * copyright(c) 2018-2021 tabuyos all right reserved.
 */
package com.tabuyos.oauth2.enums;

/**
 * ErrorType
 *
 * @author tabuyos
 * @since 2021/12/12
 */
public enum ErrorType {

  /**
   * invalid_request
   */
  INVALID_REQUEST("invalid_request"),
  /**
   * unauthorized_client
   */
  UNAUTHORIZED_CLIENT("unauthorized_client"),
  /**
   * access_denied
   */
  ACCESS_DENIED("access_denied"),
  /**
   * unsupported_response_type
   */
  UNSUPPORTED_RESPONSE_TYPE("unsupported_response_type"),
  /**
   * invalid_scope
   */
  INVALID_SCOPE("invalid_scope"),
  /**
   * server_error
   */
  SERVER_ERROR("server_error"),
  /**
   * temporarily_unavailable
   */
  TEMPORARILY_UNAVAILABLE("temporarily_unavailable");

  /**
   * error type for oauth 2.1
   */
  public final String type;

  ErrorType(String type) {
    this.type = type;
  }
}
