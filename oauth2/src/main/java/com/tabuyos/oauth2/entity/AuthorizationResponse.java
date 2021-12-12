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
 * AuthorizationResponse
 *
 * @author tabuyos
 * @since 2021/12/12
 */
@Oauth2Mime(mime = MimeType.APPLICATION_X_WWW_FORM_URLENCODED)
public class AuthorizationResponse {

  /**
   * code
   * <p>
   * an opaque value used by the client to maintain state between the request and callback.
   * the authorization server includes this value when redirecting the user-agent back to the client.
   */
  @Oauth2Parameter(conventions = NotationalConventions.REQUIRED, fieldName = "code")
  private String code;
  /**
   * state
   * <p>
   * if the "state" parameter was present in the client authorization request.  The exact value received from the client.
   */
  @Oauth2Parameter(conventions = NotationalConventions.OPTIONAL, fieldName = "state")
  private String state;

  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public String getState() {
    return state;
  }

  public void setState(String state) {
    this.state = state;
  }

  @Override
  public String toString() {
    return new StringJoiner(", ", AuthorizationResponse.class.getSimpleName() + "(", ")")
      .add("code='" + code + "'")
      .add("state='" + state + "'")
      .toString();
  }
}
