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
 * AccessTokenRequest
 *
 * @author tabuyos
 * @since 2021/12/12
 */
@Oauth2Mime(mime = MimeType.APPLICATION_X_WWW_FORM_URLENCODED)
public class AccessTokenRequest {

  /**
   * grant type
   * <p>
   * value must be set to "authorization_code"
   */
  @Oauth2Parameter(conventions = NotationalConventions.REQUIRED, fieldName = "grant_type")
  private String grantType;
  /**
   * code
   * <p>
   * the authorization code received from the authorization server.
   */
  @Oauth2Parameter(conventions = {NotationalConventions.REQUIRED, NotationalConventions.OPTIONAL}, fieldName = "code")
  private String code;
  /**
   * username
   * <p>
   * the resource owner username.
   */
  @Oauth2Parameter(conventions = {NotationalConventions.REQUIRED, NotationalConventions.OPTIONAL}, fieldName = "username")
  private String username;
  /**
   * password
   * <p>
   * the resource owner password.
   */
  @Oauth2Parameter(conventions = {NotationalConventions.REQUIRED, NotationalConventions.OPTIONAL}, fieldName = "password")
  private String password;
  /**
   * redirect uri
   * <p>
   * if the "redirect_uri" parameter was included in the authorization request, and their values must be identical.
   */
  @Oauth2Parameter(conventions = {NotationalConventions.REQUIRED, NotationalConventions.OPTIONAL}, fieldName = "redirect_uri")
  private String redirectUri;
  /**
   * client id
   * <p>
   * the client identifier as described
   */
  @Oauth2Parameter(conventions = NotationalConventions.REQUIRED, fieldName = "client_id")
  private String clientId;
  /**
   * code verifier
   * <p>
   * if the "code_challenge" parameter was included in the authorization request. must not be used otherwise. the original code verifier string.
   */
  @Oauth2Parameter(conventions = {NotationalConventions.REQUIRED, NotationalConventions.OPTIONAL}, fieldName = "code_verifier")
  private String codeVerifier;
  /**
   * scope
   * <p>
   * the scope of the access request as described
   */
  @Oauth2Parameter(conventions = NotationalConventions.OPTIONAL, fieldName = "scope")
  private String scope;

  public String getGrantType() {
    return grantType;
  }

  public void setGrantType(String grantType) {
    this.grantType = grantType;
  }

  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getRedirectUri() {
    return redirectUri;
  }

  public void setRedirectUri(String redirectUri) {
    this.redirectUri = redirectUri;
  }

  public String getClientId() {
    return clientId;
  }

  public void setClientId(String clientId) {
    this.clientId = clientId;
  }

  public String getCodeVerifier() {
    return codeVerifier;
  }

  public void setCodeVerifier(String codeVerifier) {
    this.codeVerifier = codeVerifier;
  }

  public String getScope() {
    return scope;
  }

  public void setScope(String scope) {
    this.scope = scope;
  }

  @Override
  public String toString() {
    return new StringJoiner(", ", AccessTokenRequest.class.getSimpleName() + "(", ")")
      .add("grantType='" + grantType + "'")
      .add("code='" + code + "'")
      .add("username='" + username + "'")
      .add("password='" + password + "'")
      .add("redirectUri='" + redirectUri + "'")
      .add("clientId='" + clientId + "'")
      .add("codeVerifier='" + codeVerifier + "'")
      .add("scope='" + scope + "'")
      .toString();
  }
}
