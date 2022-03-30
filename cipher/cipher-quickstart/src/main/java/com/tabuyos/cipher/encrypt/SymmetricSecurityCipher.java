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
      // 创建密码器
      Cipher cipher = Cipher.getInstance(secretKeySpec.getAlgorithm());
      byte[] byteContent = content.getBytes(StandardCharsets.UTF_8);
      // 初始化为加密模式的密码器
      cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
      // 加密
      byte[] result = cipher.doFinal(byteContent);
      // 二进制转换成16进制字符串
      return Base64.getEncoder().encodeToString(result);
    } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException e) {
      logger.error("encrypt with error! ", e);
      return null;
    }
  }

  @Override
  public String decrypt(String content) {
    try {
      // 十六进制字符串转换成二进制字节数组
      byte[] bytes = Base64.getDecoder().decode(content.getBytes(StandardCharsets.UTF_8));
      Cipher cipher = Cipher.getInstance(secretKeySpec.getAlgorithm());
      cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);
      // 解密
      byte[] result = cipher.doFinal(bytes);
      return new String(result, StandardCharsets.UTF_8);
    } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException e) {
      logger.error("decrypt with error! ", e);
      return null;
    }
  }

  private static SecretKeySpec generateKey(String secret, String algorithm, int keySize) {
    try {
      // 创建 AES 的 Key 生产者
      KeyGenerator keyGenerator = KeyGenerator.getInstance(algorithm);
      // 利用用户密码作为随机数初始化出
      // SecureRandom 是生成安全随机数序列, secret.getBytes() 是种子,
      // 只要种子相同, 序列就一样, 所以解密只要有 secret 就行
      keyGenerator.init(keySize, new SecureRandom(secret.getBytes()));
      // 根据用户密码, 生成一个密钥
      SecretKey secretKey = keyGenerator.generateKey();
      // 返回基本编码格式的密钥, 如果此密钥不支持编码, 则返回
      byte[] enCodeFormat = secretKey.getEncoded();
      // 转换为AES专用密钥
      return new SecretKeySpec(enCodeFormat, algorithm);
    } catch (NoSuchAlgorithmException e) {
      logger.error("no such algorithm exception! ", e);
      return null;
    }
  }
}
