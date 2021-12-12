/*
 * copyright(c) 2018-2021 tabuyos all right reserved.
 */
package com.tabuyos.oauth2.annotation;

import com.tabuyos.oauth2.enums.MimeType;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Oauth2Mime
 *
 * @author tabuyos
 * @since 2021/12/10
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface Oauth2Mime {
  /**
   * mime type of oauth2
   *
   * @return mime type
   */
  MimeType[] mime();
}
