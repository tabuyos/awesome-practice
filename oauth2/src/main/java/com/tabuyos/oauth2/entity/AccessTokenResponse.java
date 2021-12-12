/*
 * copyright(c) 2018-2021 tabuyos all right reserved.
 */
package com.tabuyos.oauth2.entity;

import com.tabuyos.oauth2.annotation.Oauth2Mime;
import com.tabuyos.oauth2.annotation.Oauth2Parameter;
import com.tabuyos.oauth2.enums.MimeType;
import com.tabuyos.oauth2.enums.NotationalConventions;

import java.util.StringJoiner;

/**
 * AccessTokenResponse
 *
 * @author tabuyos
 * @since 2021/12/12
 */
@Oauth2Mime(mime = MimeType.APPLICATION_JSON)
public class AccessTokenResponse {
  /**
   * access_token
   */
  @Oauth2Parameter(conventions = NotationalConventions.REQUIRED, fieldName = "access_token")
  private String accessToken;
  /**
   * token_type
   */
  @Oauth2Parameter(conventions = NotationalConventions.REQUIRED, fieldName = "token_type")
  private String tokenType;
  /**
   * expires_in
   */
  @Oauth2Parameter(conventions = NotationalConventions.REQUIRED, fieldName = "expires_in")
  private String expiresIn;
  /**
   * refresh_token
   */
  @Oauth2Parameter(conventions = NotationalConventions.REQUIRED, fieldName = "refresh_token")
  private String refreshToken;
  /**
   * payload
   */
  @Oauth2Parameter(conventions = NotationalConventions.OPTIONAL, fieldName = "payload")
  private String payload;

  public String getAccessToken() {
    return accessToken;
  }

  public void setAccessToken(String accessToken) {
    this.accessToken = accessToken;
  }

  public String getTokenType() {
    return tokenType;
  }

  public void setTokenType(String tokenType) {
    this.tokenType = tokenType;
  }

  public String getExpiresIn() {
    return expiresIn;
  }

  public void setExpiresIn(String expiresIn) {
    this.expiresIn = expiresIn;
  }

  public String getRefreshToken() {
    return refreshToken;
  }

  public void setRefreshToken(String refreshToken) {
    this.refreshToken = refreshToken;
  }

  public String getPayload() {
    return payload;
  }

  public void setPayload(String payload) {
    this.payload = payload;
  }

  @Override
  public String toString() {
    return new StringJoiner(", ", AccessTokenResponse.class.getSimpleName() + "(", ")")
      .add("accessToken='" + accessToken + "'")
      .add("tokenType='" + tokenType + "'")
      .add("expiresIn='" + expiresIn + "'")
      .add("refreshToken='" + refreshToken + "'")
      .add("payload='" + payload + "'")
      .toString();
  }
}
