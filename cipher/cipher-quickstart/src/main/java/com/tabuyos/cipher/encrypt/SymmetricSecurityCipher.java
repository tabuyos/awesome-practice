/*
 * Copyright (c) 2018-2021 Tabuyos All Right Reserved.
 */
package com.tabuyos.cipher.encrypt;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Optional;

/**
 * SymmetricCipher
 *
 * @author tabuyos
 * @since 2021/12/15
 */
@SuppressWarnings({"unused", "ClassCanBeRecord"})
public class SymmetricSecurityCipher implements SecurityCipher {

  private static final Logger logger = LoggerFactory.getLogger(SymmetricSecurityCipher.class);

  private static final String DEFAULT_ALGORITHM = "AES";
  private static final Integer DEFAULT_KEY_SIZE = 256;
  private static final String DEFAULT_SECRET = "secret-string";

  private final SecretKeySpec secretKeySpec;

  public SymmetricSecurityCipher(SecretKeySpec secretKeySpec) {
    this.secretKeySpec = secretKeySpec;
  }

  public static SymmetricSecurityCipher of(String secret) {
    return new SymmetricSecurityCipher(
        generateKey(
            Optional.ofNullable(secret).orElse(DEFAULT_SECRET),
            DEFAULT_ALGORITHM,
            DEFAULT_KEY_SIZE));
  }

  public static SymmetricSecurityCipher of(String secret, String algorithm) {
    return new SymmetricSecurityCipher(
        generateKey(
            Optional.ofNullable(secret).orElse(DEFAULT_SECRET),
            Optional.ofNullable(algorithm).orElse(DEFAULT_ALGORITHM),
            DEFAULT_KEY_SIZE));
  }

  public static SymmetricSecurityCipher of(String secret, String algorithm, Integer keySize) {
    return new SymmetricSecurityCipher(
        generateKey(
            Optional.ofNullable(secret).orElse(DEFAULT_SECRET),
            Optional.ofNullable(algorithm).orElse(DEFAULT_ALGORITHM),
            Optional.ofNullable(keySize).orElse(DEFAULT_KEY_SIZE)));
  }

  @Override
  public String encrypt(String content) {
    try {
      // ???????????????
      Cipher cipher = Cipher.getInstance(secretKeySpec.getAlgorithm());
      byte[] byteContent = content.getBytes(StandardCharsets.UTF_8);
      // ????????????????????????????????????
      cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
      // ??????
      byte[] result = cipher.doFinal(byteContent);
      // ??????????????????16???????????????
      return Base64.getEncoder().encodeToString(result);
    } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException e) {
      logger.error("encrypt with error! ", e);
      return null;
    }
  }

  @Override
  public String decrypt(String content) {
    try {
      // ???????????????????????????????????????????????????
      byte[] bytes = Base64.getDecoder().decode(content.getBytes(StandardCharsets.UTF_8));
      Cipher cipher = Cipher.getInstance(secretKeySpec.getAlgorithm());
      cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);
      // ??????
      byte[] result = cipher.doFinal(bytes);
      return new String(result, StandardCharsets.UTF_8);
    } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException e) {
      logger.error("decrypt with error! ", e);
      return null;
    }
  }

  private static SecretKeySpec generateKey(String secret, String algorithm, int keySize) {
    try {
      // ?????? AES ??? Key ?????????
      KeyGenerator keyGenerator = KeyGenerator.getInstance(algorithm);
      // ?????????????????????????????????????????????
      // SecureRandom ??????????????????????????????, secret.getBytes() ?????????,
      // ??????????????????, ???????????????, ????????????????????? secret ??????
      keyGenerator.init(keySize, new SecureRandom(secret.getBytes()));
      // ??????????????????, ??????????????????
      SecretKey secretKey = keyGenerator.generateKey();
      // ?????????????????????????????????, ??????????????????????????????, ?????????
      byte[] enCodeFormat = secretKey.getEncoded();
      // ?????????AES????????????
      return new SecretKeySpec(enCodeFormat, algorithm);
    } catch (NoSuchAlgorithmException e) {
      logger.error("no such algorithm exception! ", e);
      return null;
    }
  }
}
