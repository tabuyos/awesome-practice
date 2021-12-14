/*
 * Copyright (c) 2018-2021 Tabuyos All Right Reserved.
 */
package com.tabuyos.jwt.service;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.jwk.RSAKey;
import com.tabuyos.jwt.entity.PayloadData;

import java.text.ParseException;

/**
 * JwtTokenService
 *
 * @author tabuyos
 * @since 2021/12/14
 */
public interface JwtTokenService {

  /**
   * generate token by HMAC
   *
   * @param payloadData payload data
   * @param secret secret info
   * @return token hmac
   * @throws JOSEException JOSE ex
   */
  String generateTokenByHmac(String payloadData, String secret) throws JOSEException;

  /**
   * verify token by HMAC
   *
   * @param token pending verify token
   * @param secret secret info
   * @return Payload data object
   * @throws ParseException parse ex
   * @throws JOSEException JOSE ex
   */
  PayloadData verifyTokenByHmac(String token, String secret) throws ParseException, JOSEException;

  /**
   * 使用RSA算法生成token
   *
   * @param payloadData payload data
   * @param rsaKey rsa key
   * @return token rsa
   * @throws JOSEException JOSE ex
   */
  String generateTokenByRsa(String payloadData, RSAKey rsaKey) throws JOSEException;

  /**
   * 使用RSA算法校验token
   *
   * @param token token string
   * @param rsaKey rsa key
   * @return payload data object
   * @throws ParseException parse ex
   * @throws JOSEException jose ex
   */
  PayloadData verifyTokenByRsa(String token, RSAKey rsaKey)
      throws ParseException, JOSEException;

  /**
   * 获取默认payload
   *
   * @return payload data object
   */
  PayloadData getDefaultPayloadData();

  /**
   * 获取默认的RSAKey
   *
   * @return rsa key
   * @throws Exception ex
   */
  RSAKey getDefaultRsaKey() throws Exception;
}
