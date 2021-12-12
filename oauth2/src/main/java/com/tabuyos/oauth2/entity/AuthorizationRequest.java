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
 * AuthorizationRequest
 *
 * @author tabuyos
 * @since 2021/12/11
 */
@Oauth2Mime(mime = MimeType.APPLICATION_X_WWW_FORM_URLENCODED)
public class AuthorizationRequest {

  /**
   * response type
   * <p>
   * value must be set to "code"
   */
  @Oauth2Parameter(conventions = NotationalConventions.REQUIRED, fieldName = "response_type")
  private String responseType;
  /**
   * client id
   * <p>
   * the client identifier as described
   */
  @Oauth2Parameter(conventions = NotationalConventions.REQUIRED, fieldName = "client_id")
  private String clientId;
  /**
   * code challenge
   */
  @Oauth2Parameter(conventions = {NotationalConventions.REQUIRED, NotationalConventions.RECOMMENDED}, fieldName = "code_challenge")
  private String codeChallenge;
  /**
   * code challenge method
   * <p>
   * defaults to "plain" if not present in the request.  Code verifier transformation method is "S256" or "plain".
   */
  @Oauth2Parameter(conventions = NotationalConventions.OPTIONAL, fieldName = "code_challenge_method")
  private String codeChallengeMethod;
  /**
   * redirect uri
   */
  @Oauth2Parameter(conventions = NotationalConventions.OPTIONAL, fieldName = "redirect_uri")
  private String redirectUri;
  /**
   * scope
   * <p>
   * the scope of the access request as described
   */
  @Oauth2Parameter(conventions = NotationalConventions.OPTIONAL, fieldName = "scope")
  private String scope;
  /**
   * state
   * <p>
   * an opaque value used by the client to maintain state between the request and callback.
   * the authorization server includes this value when redirecting the user-agent back to the client.
   */
  @Oauth2Parameter(conventions = NotationalConventions.OPTIONAL, fieldName = "state")
  private String state;

  public String getResponseType() {
    return responseType;
  }

  public void setResponseType(String responseType) {
    this.responseType = responseType;
  }

  public String getClientId() {
    return clientId;
  }

  public void setClientId(String clientId) {
    this.clientId = clientId;
  }

  public String getCodeChallenge() {
    return codeChallenge;
  }

  public void setCodeChallenge(String codeChallenge) {
    this.codeChallenge = codeChallenge;
  }

  public String getCodeChallengeMethod() {
    return codeChallengeMethod;
  }

  public void setCodeChallengeMethod(String codeChallengeMethod) {
    this.codeChallengeMethod = codeChallengeMethod;
  }

  public String getRedirectUri() {
    return redirectUri;
  }

  public void setRedirectUri(String redirectUri) {
    this.redirectUri = redirectUri;
  }

  public String getScope() {
    return scope;
  }

  public void setScope(String scope) {
    this.scope = scope;
  }

  public String getState() {
    return state;
  }

  public void setState(String state) {
    this.state = state;
  }

  @Override
  public String toString() {
    return new StringJoiner(", ", AuthorizationRequest.class.getSimpleName() + "(", ")")
      .add("responseType='" + responseType + "'")
      .add("clientId='" + clientId + "'")
      .add("codeChallenge='" + codeChallenge + "'")
      .add("codeChallengeMethod='" + codeChallengeMethod + "'")
      .add("redirectUri='" + redirectUri + "'")
      .add("scope='" + scope + "'")
      .add("state='" + state + "'")
      .toString();
  }
}
