/*
 * copyright(c) 2018-2021 tabuyos all right reserved.
 */
package com.tabuyos.oauth2.entity;

import com.tabuyos.oauth2.annotation.Oauth2Mime;
import com.tabuyos.oauth2.annotation.Oauth2Parameter;
import com.tabuyos.oauth2.enums.ErrorType;
import com.tabuyos.oauth2.enums.MimeType;
import com.tabuyos.oauth2.enums.NotationalConventions;

import java.util.StringJoiner;

/**
 * ErrorResponse
 *
 * @author tabuyos
 * @since 2021/12/12
 */
@Oauth2Mime(mime = MimeType.APPLICATION_X_WWW_FORM_URLENCODED)
public class ErrorResponse {
  /**
   * error
   *
   * Values for the "error" parameter MUST NOT include
   * characters outside the set %x20-21 / %x23-5B / %x5D-7E.
   */
  @Oauth2Parameter(conventions = NotationalConventions.REQUIRED, fieldName = "error")
  private ErrorType error;
  /**
   * error_description
   * <p>
   * Human-readable ASCII text providing additional information,
   * used to assist the client developer in understanding the error that occurred.
   * Values for the "error_description" parameter MUST NOT include
   * characters outside the set %x20-21 / %x23-5B / %x5D-7E.
   */
  @Oauth2Parameter(conventions = NotationalConventions.OPTIONAL, fieldName = "error_description")
  private String errorDescription;
  /**
   * error_uri
   * <p>
   * A URI identifying a human-readable web page with information about the error,
   * used to provide the client developer with additional information about the error.
   * Values for the "error_uri" parameter MUST conform to the URI-reference syntax and
   * thus MUST NOT include characters outside the set %x21 / %x23-5B / %x5D-7E.
   */
  @Oauth2Parameter(conventions = NotationalConventions.OPTIONAL, fieldName = "error_uri")
  private String errorUri;
  /**
   * state
   * <p>
   * if the "state" parameter was present in the client authorization request.  The exact value received from the client.
   */
  @Oauth2Parameter(conventions = NotationalConventions.OPTIONAL, fieldName = "state")
  private String state;

  public ErrorType getError() {
    return error;
  }

  public void setError(ErrorType error) {
    this.error = error;
  }

  public String getErrorDescription() {
    return errorDescription;
  }

  public void setErrorDescription(String errorDescription) {
    this.errorDescription = errorDescription;
  }

  public String getErrorUri() {
    return errorUri;
  }

  public void setErrorUri(String errorUri) {
    this.errorUri = errorUri;
  }

  public String getState() {
    return state;
  }

  public void setState(String state) {
    this.state = state;
  }

  @Override
  public String toString() {
    return new StringJoiner(", ", ErrorResponse.class.getSimpleName() + "(", ")")
      .add("error=" + error)
      .add("errorDescription='" + errorDescription + "'")
      .add("errorUri='" + errorUri + "'")
      .add("state='" + state + "'")
      .toString();
  }
}
