/*
 * Copyright (c) 2018-2021 Tabuyos All Right Reserved.
 */
package com.tabuyos.jwt.service;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.UUID;
import cn.hutool.json.JSONUtil;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JOSEObjectType;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSObject;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.Payload;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jose.crypto.RSASSAVerifier;
import com.nimbusds.jose.jwk.RSAKey;
import com.tabuyos.jwt.entity.PayloadData;
import com.tabuyos.jwt.exception.JwtExpiredException;
import com.tabuyos.jwt.exception.JwtInvalidException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.KeyPair;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.UnrecoverableKeyException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;

/**
 * JwtTokenServiceImpl
 *
 * @author tabuyos
 * @since 2021/12/14
 */
@SuppressWarnings("SameParameterValue")
@Service
public class JwtTokenServiceImpl implements JwtTokenService {

  private static final Logger logger = LoggerFactory.getLogger(JwtTokenServiceImpl.class);

  @Override
  public String generateTokenByHmac(String payloadData, String secret) throws JOSEException {
    // 创建JWS头，设置签名算法和类型
    JWSHeader jwsHeader =
        new JWSHeader.Builder(JWSAlgorithm.HS256).type(JOSEObjectType.JWT).build();
    // 将负载信息封装到Payload中
    Payload payload = new Payload(payloadData);
    // 创建JWS对象
    JWSObject jwsObject = new JWSObject(jwsHeader, payload);
    // 创建HMAC签名器
    JWSSigner jwsSigner = new MACSigner(secret);
    // 签名
    jwsObject.sign(jwsSigner);
    return jwsObject.serialize();
  }

  @Override
  public PayloadData verifyTokenByHmac(String token, String secret)
      throws JOSEException, ParseException {
    // 从token中解析JWS对象
    JWSObject jwsObject = JWSObject.parse(token);
    // 创建HMAC验证器
    JWSVerifier jwsVerifier = new MACVerifier(secret);
    if (!jwsObject.verify(jwsVerifier)) {
      logger.error("token 签名不合法!");
      throw new JwtInvalidException("token 签名不合法!");
    }
    String payload = jwsObject.getPayload().toString();
    PayloadData payloadData = JSONUtil.toBean(payload, PayloadData.class);
    if (payloadData.getExpire() < Calendar.getInstance().getTime().getTime()) {
      logger.error("token 已过期!");
      throw new JwtExpiredException("token 已过期!");
    }
    return payloadData;
  }

  @Override
  public String generateTokenByRsa(String payloadData, RSAKey rsaKey) throws JOSEException {
    // 创建JWS头，设置签名算法和类型
    JWSHeader jwsHeader =
        new JWSHeader.Builder(JWSAlgorithm.RS256).type(JOSEObjectType.JWT).build();
    // 将负载信息封装到Payload中
    Payload payload = new Payload(payloadData);
    // 创建JWS对象
    JWSObject jwsObject = new JWSObject(jwsHeader, payload);
    // 创建RSA签名器
    JWSSigner jwsSigner = new RSASSASigner(rsaKey);
    // 签名
    jwsObject.sign(jwsSigner);
    return jwsObject.serialize();
  }

  @Override
  public PayloadData verifyTokenByRsa(String token, RSAKey rsaKey)
      throws ParseException, JOSEException {
    // 从token中解析JWS对象
    JWSObject jwsObject = JWSObject.parse(token);
    RSAKey publicRsaKey = rsaKey.toPublicJWK();
    // 使用RSA公钥创建RSA验证器
    JWSVerifier jwsVerifier = new RSASSAVerifier(publicRsaKey);
    if (!jwsObject.verify(jwsVerifier)) {
      throw new JwtInvalidException("token签名不合法！");
    }
    String payload = jwsObject.getPayload().toString();
    PayloadData payloadData = JSONUtil.toBean(payload, PayloadData.class);
    if (payloadData.getExpire() < Calendar.getInstance().getTime().getTime()) {
      throw new JwtExpiredException("token已过期！");
    }
    return payloadData;
  }

  @Override
  public PayloadData getDefaultPayloadData() {
    Date now = new Date();
    Date exp = DateUtil.offsetSecond(now, 60 * 60);
    return PayloadData.builder()
        .subject("tabuyos")
        .issueAt(now.getTime())
        .expire(exp.getTime())
        .jwtId(UUID.randomUUID().toString())
        .username("tabuyos")
        .authorities(CollUtil.toList("DEV"))
        .build();
  }

  @Override
  public RSAKey getDefaultRsaKey() throws Exception {
    // 从classpath下获取RSA秘钥对
    KeyPair keyPair = getKeyPair("jwt.jks", "123456".toCharArray());
    // 获取RSA公钥
    RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
    // 获取RSA私钥
    RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
    return new RSAKey.Builder(publicKey).privateKey(privateKey).build();
  }

  private KeyPair getKeyPair(String fileLocation, char[] password)
      throws UnrecoverableKeyException, KeyStoreException, NoSuchAlgorithmException, IOException,
          CertificateException {
    KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
    keyStore.load(new ClassPathResource("jwt.jks").getInputStream(), "123456".toCharArray());
    Enumeration<String> aliases = keyStore.aliases();
    String alias = null;
    while (aliases.hasMoreElements()) {
      alias = aliases.nextElement();
    }
    PrivateKey privateKey = (PrivateKey) keyStore.getKey(alias, "123456".toCharArray());
    Certificate certificate = keyStore.getCertificate(alias);
    PublicKey publicKey = certificate.getPublicKey();
    return new KeyPair(publicKey, privateKey);
  }
}
