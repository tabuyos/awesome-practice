/*
 * copyright(c) 2018-2021 tabuyos all right reserved.
 */
package com.tabuyos.oauth2.annotation;

import com.tabuyos.oauth2.enums.NotationalConventions;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Oauth2Parameter
 *
 * @author tabuyos
 * @since 2021/12/10
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface Oauth2Parameter {
  /**
   * conventions
   *
   * @return conventions
   */
  NotationalConventions[] conventions() default NotationalConventions.OPTIONAL;

  /**
   * field name
   *
   * @return field name
   */
  String[] fieldName() default {""};
}
