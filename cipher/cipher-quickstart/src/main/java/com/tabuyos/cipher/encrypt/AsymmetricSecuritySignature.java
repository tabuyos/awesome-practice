/*
 * Copyright (c) 2018-2021 Tabuyos All Right Reserved.
 */
package com.tabuyos.cipher.encrypt;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.Signature;
import java.security.SignatureException;
import java.util.Base64;
import java.util.Optional;

/**
 * AsymmetricSecuritySignature
 *
 * @author tabuyos
 * @since 2021/12/15
 */
@SuppressWarnings({"unused", "ClassCanBeRecord"})
public class AsymmetricSecuritySignature implements SecuritySignature {

  private static final Logger logger = LoggerFactory.getLogger(AsymmetricSecuritySignature.class);

  private static final String DEFAULT_ALGORITHM = "SHA256withRSA";

  private final AsymmetricSecurityCipher securityCipher;
  private final String algorithm;

  public AsymmetricSecuritySignature(AsymmetricSecurityCipher securityCipher, String algorithm) {
    this.securityCipher = securityCipher;
    this.algorithm = algorithm;
  }

  public static AsymmetricSecuritySignature of(AsymmetricSecurityCipher securityCipher) {
    return new AsymmetricSecuritySignature(securityCipher, DEFAULT_ALGORITHM);
  }

  public static AsymmetricSecuritySignature of(
      AsymmetricSecurityCipher securityCipher, String algorithm) {
    return new AsymmetricSecuritySignature(
        securityCipher, Optional.ofNullable(algorithm).orElse(DEFAULT_ALGORITHM));
  }

  @Override
  public String sign(String content) {
    try {
      Signature privateSignature = Signature.getInstance(algorithm);
      privateSignature.initSign(securityCipher.getPrivateKeyFromP12());
      privateSignature.update(content.getBytes(StandardCharsets.UTF_8));
      byte[] signature = privateSignature.sign();
      return Base64.getEncoder().encodeToString(signature);
    } catch (NoSuchAlgorithmException | InvalidKeyException | SignatureException e) {
      logger.error("sign with error! ", e);
      return null;
    }
  }

  @Override
  public boolean verify(String content, String signature) {
    try {
      byte[] decode = Base64.getDecoder().decode(signature);
      Signature publicSignature = Signature.getInstance(algorithm);
      publicSignature.initVerify(securityCipher.getPublicKeyFromP12());
      publicSignature.update(content.getBytes(StandardCharsets.UTF_8));
      return publicSignature.verify(decode);
    } catch (NoSuchAlgorithmException | InvalidKeyException | SignatureException e) {
      logger.error("verify with error! ", e);
      return false;
    }
  }
}
