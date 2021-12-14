/*
 * Copyright (c) 2018-2021 Tabuyos All Right Reserved.
 */
package com.tabuyos.jwt.controller;

import cn.hutool.crypto.SecureUtil;
import cn.hutool.json.JSONUtil;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.tabuyos.jwt.entity.PayloadData;
import com.tabuyos.jwt.service.JwtTokenService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import java.util.Map;

/**
 * JwtTokenController
 *
 * @author tabuyos
 * @since 2021/12/14
 */
@RestController
@RequestMapping("/token")
public class JwtTokenController {

  private final JwtTokenService jwtTokenService;

  public JwtTokenController(JwtTokenService jwtTokenService) {
    this.jwtTokenService = jwtTokenService;
  }

  /**
   * 使用对称加密（HMAC）算法生成token
   *
   * @return map
   */
  @GetMapping("/hmac/generate")
  public Map<String, Object> generateTokenByHmac() {
    try {
      PayloadData payloadData = jwtTokenService.getDefaultPayloadData();
      String token =
          jwtTokenService.generateTokenByHmac(
              JSONUtil.toJsonStr(payloadData), SecureUtil.md5("test"));
      return Map.of("success", token);
    } catch (JOSEException e) {
      e.printStackTrace();
    }
    return Map.of("failed", "true");
  }

  /**
   * 使用对称加密（HMAC）算法验证token
   *
   * @param token token
   * @return map
   */
  @GetMapping("/hmac/verify")
  public Map<String, Object> verifyTokenByHmac(String token) {
    try {
      PayloadData payloadData = jwtTokenService.verifyTokenByHmac(token, SecureUtil.md5("test"));
      return Map.of("success", payloadData);
    } catch (ParseException | JOSEException e) {
      e.printStackTrace();
    }
    return Map.of("failed", "true");
  }

  /**
   * 使用非对称加密（RSA）算法生成token
   *
   * @return map
   */
  @GetMapping("/rsa/generate")
  public Map<String, Object> generateTokenByRsa() {
    try {
      PayloadData payloadData = jwtTokenService.getDefaultPayloadData();
      String token =
          jwtTokenService.generateTokenByRsa(
              JSONUtil.toJsonStr(payloadData), jwtTokenService.getDefaultRsaKey());
      return Map.of("success", token);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return Map.of("failed", "true");
  }

  /**
   * 使用非对称加密（RSA）算法验证token
   *
   * @param token token
   * @return map
   */
  @GetMapping("/rsa/verify")
  public Map<String, Object> verifyTokenByRsa(String token) {
    try {
      PayloadData payloadData =
          jwtTokenService.verifyTokenByRsa(token, jwtTokenService.getDefaultRsaKey());
      return Map.of("success", payloadData);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return Map.of("failed", "true");
  }

  /**
   * 获取非对称加密（RSA）算法公钥
   *
   * @return object
   */
  @GetMapping("/rsa/publicKey")
  public Object getRsaPublicKey() throws Exception {
    RSAKey rsaKey = jwtTokenService.getDefaultRsaKey();
    return new JWKSet(rsaKey).toJSONObject();
  }

  @GetMapping("test")
  public String test(@RequestParam("un") String username) {
    return username;
  }
}
