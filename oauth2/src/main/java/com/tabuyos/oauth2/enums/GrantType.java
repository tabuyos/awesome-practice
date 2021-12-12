/*
 * copyright(c) 2018-2021 tabuyos all right reserved.
 */
package com.tabuyos.oauth2.enums;

/**
 * Oauth2GrantTypeEnum
 *
 * @author tabuyos
 * @since 2021/12/10
 */
public enum GrantType {

  /**
   * Authorization Code Grant
   *
   * authorization_code
   */
  AUTHORIZATION_CODE("authorization_code"),
  /**
   * Implicit Grant
   *
   * implicit
   */
  IMPLICIT("implicit"),
  /**
   * Refreshing Access Token
   *
   * refresh_token
   */
  REFRESH_TOKEN("refresh_token"),
  /**
   * Client Credentials Grant
   *
   * client_credentials
   */
  CLIENT_CREDENTIALS("client_credentials"),
  /**
   * Resource Owner Password Credentials Grant
   *
   * password
   */
  PASSWORD("password"),
  /**
   * Extension Grants
   *
   * urn:ietf:params:oauth:grant-type:jwt-bearer
   */
  JWT_BEARER("urn:ietf:params:oauth:grant-type:jwt-bearer");

  /**
   * grant type for oauth 2.1
   */
  public final String type;

  GrantType(String type) {
    this.type = type;
  }
}
