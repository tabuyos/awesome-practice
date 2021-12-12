/*
 * copyright(c) 2018-2021 tabuyos all right reserved.
 */
package com.tabuyos.oauth2.resolver;

import com.tabuyos.oauth2.annotation.Oauth2Mime;
import com.tabuyos.oauth2.annotation.Oauth2Parameter;
import com.tabuyos.oauth2.enums.MimeType;
import com.tabuyos.oauth2.enums.NotationalConventions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Map;
import java.util.Optional;

/**
 * HandlerOauth2ArgumentResolver
 *
 * @author tabuyos
 * @since 2021/12/12
 */
@SuppressWarnings("ClassCanBeRecord")
public class HandlerOauth2ArgumentResolver implements HandlerMethodArgumentResolver {

  private static final Logger logger = LoggerFactory.getLogger(HandlerOauth2ArgumentResolver.class);

  private final Class<?> resolverClass;

  public HandlerOauth2ArgumentResolver(Class<?> resolverClass) {
    this.resolverClass = resolverClass;
  }

  @Override
  public boolean supportsParameter(MethodParameter parameter) {
    return parameter.getParameterType() == resolverClass;
  }

  @Override
  public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
    HttpServletRequest servletRequest = webRequest.getNativeRequest(HttpServletRequest.class);
    if (servletRequest == null) {
      return null;
    }
    MimeType mimeType = MimeType.APPLICATION_X_WWW_FORM_URLENCODED;
    String header = servletRequest.getHeader(HttpHeaders.CONTENT_TYPE);
    Class<?> clazz = parameter.getParameterType();
    if (clazz.isAnnotationPresent(Oauth2Mime.class)) {
      Oauth2Mime oauth2Mime = clazz.getDeclaredAnnotation(Oauth2Mime.class);
      mimeType = Arrays.stream(oauth2Mime.mime()).findFirst().orElse(MimeType.APPLICATION_X_WWW_FORM_URLENCODED);
    }
    if (!mimeType.type.equalsIgnoreCase(header)) {
      return null;
    }
    Map<String, String[]> parameterMap = servletRequest.getParameterMap();
    Object result = resolverClass.getConstructor().newInstance();
    Field[] fields = clazz.getDeclaredFields();
    Arrays.stream(fields).forEach(field -> {
      if (!field.isAnnotationPresent(Oauth2Parameter.class)) {
        return;
      }
      Oauth2Parameter oauth2Parameter = field.getAnnotation(Oauth2Parameter.class);
      NotationalConventions[] conventions = oauth2Parameter.conventions();
//      boolean requiredMatch = Arrays.stream(conventions).anyMatch(convention -> convention == NotationalConventions.REQUIRED);
//      boolean recommendedMatch = Arrays.stream(conventions).anyMatch(convention -> convention == NotationalConventions.RECOMMENDED);
      String[] fieldNames = oauth2Parameter.fieldName();
      Arrays.stream(fieldNames).forEach(name -> {
        String[] values = parameterMap.get(name);
        Optional<String> first = Arrays.stream(Optional.ofNullable(values).orElse(new String[]{})).findFirst();
//        if (first.isEmpty() && (requiredMatch && !recommendedMatch)) {
//          logger.error(name + " field is required!");
//          throw new RuntimeException(name + " field is required!");
//        }
        String value = first.orElse("");
        field.setAccessible(true);
        if (field.trySetAccessible()) {
          try {
            field.set(result, value);
          } catch (IllegalAccessException e) {
            logger.error("set field with error: ", e);
            throw new RuntimeException("set field with error!");
          }
        }
      });
    });
    return result;
  }
}
