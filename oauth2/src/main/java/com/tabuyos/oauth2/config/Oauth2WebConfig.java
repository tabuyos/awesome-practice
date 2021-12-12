/*
 * copyright(c) 2018-2021 tabuyos all right reserved.
 */
package com.tabuyos.oauth2.config;

import com.tabuyos.oauth2.entity.AccessTokenRequest;
import com.tabuyos.oauth2.entity.AuthorizationRequest;
import com.tabuyos.oauth2.resolver.HandlerOauth2ArgumentResolver;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.ArrayList;
import java.util.List;

/**
 * Oauth2WebConfig
 *
 * @author tabuyos
 * @since 2021/12/12
 */
@Configuration
public class Oauth2WebConfig implements WebMvcConfigurer {

  @Override
  public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
    resolvers.addAll(oauth2ArgumentResolvers());
  }

  private List<HandlerOauth2ArgumentResolver> oauth2ArgumentResolvers() {
    List<HandlerOauth2ArgumentResolver> oauth2ArgumentResolvers = new ArrayList<>();
    oauth2ArgumentResolvers.add(new HandlerOauth2ArgumentResolver(AuthorizationRequest.class));
    oauth2ArgumentResolvers.add(new HandlerOauth2ArgumentResolver(AccessTokenRequest.class));
    return oauth2ArgumentResolvers;
  }
}
