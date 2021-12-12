/*
 * copyright(c) 2018-2021 tabuyos all right reserved.
 */
package com.tabuyos.oauth2.controller;

import com.tabuyos.oauth2.entity.AccessTokenRequest;
import com.tabuyos.oauth2.entity.AuthorizationRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Oauth2Controller
 *
 * @author tabuyos
 * @since 2021/12/10
 */
@RestController
@RequestMapping("oauth2")
public class Oauth2Controller {

  @PostMapping("authorize")
  @GetMapping("authorize")
  public AuthorizationRequest authorize(AuthorizationRequest authorizationRequest) {
    System.out.println(authorizationRequest.toString());
    return authorizationRequest;
  }

  @PostMapping("token")
  public AccessTokenRequest token(AccessTokenRequest accessTokenRequest) {
    System.out.println(accessTokenRequest.toString());
    return accessTokenRequest;
  }
}
