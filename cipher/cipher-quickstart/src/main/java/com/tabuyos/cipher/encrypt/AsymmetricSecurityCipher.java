/*
 * Copyright (c) 2018-2021 Tabuyos All Right Reserved.
 */
package com.tabuyos.cipher.encrypt;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Cipher;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.cert.Certificate;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * AsymmetricCipher
 *
 * @author tabuyos
 * @since 2021/12/15
 */
@SuppressWarnings({"unused", "ClassCanBeRecord"})
public class AsymmetricSecurityCipher implements SecurityCipher {

  private static final Logger logger = LoggerFactory.getLogger(SymmetricSecurityCipher.class);

  private static final String DEFAULT_ALGORITHM = "RSA";
  private static final String DEFAULT_LOCATION = "pkcs.p12";
  private static final String DEFAULT_PASSWORD = "tabuyos-pkcs12";
  private static final String DEFAULT_KEY_STORE_TYPE = "PKCS12";
  private static final Integer DEFAULT_KEY_SIZE = 2048;

  private final String algorithm;
  private final String location;
  private final String password;
  private final String keyStoreType;
  private final Integer keySize;

  public AsymmetricSecurityCipher(String algorithm,
                                  String location,
                                  String password,
                                  String keyStoreType,
                                  Integer keySize) {
    this.algorithm = algorithm;
    this.location = location;
    this.password = password;
    this.keyStoreType = keyStoreType;
    this.keySize = keySize;
  }

  public static AsymmetricSecurityCipher of() {
    return new AsymmetricSecurityCipher(
        DEFAULT_ALGORITHM,
        DEFAULT_LOCATION,
        DEFAULT_PASSWORD,
        DEFAULT_KEY_STORE_TYPE,
        DEFAULT_KEY_SIZE);
  }

  public static AsymmetricSecurityCipher of(Integer keySize) {
    return new AsymmetricSecurityCipher(
        DEFAULT_ALGORITHM,
        DEFAULT_LOCATION,
        DEFAULT_PASSWORD,
        DEFAULT_KEY_STORE_TYPE,
        Optional.ofNullable(keySize).orElse(DEFAULT_KEY_SIZE));
  }

  public static AsymmetricSecurityCipher of(String location, String password) {
    return new AsymmetricSecurityCipher(
        DEFAULT_ALGORITHM,
        Optional.ofNullable(location).orElse(DEFAULT_LOCATION),
        Optional.ofNullable(password).orElse(DEFAULT_PASSWORD),
        DEFAULT_KEY_STORE_TYPE,
        DEFAULT_KEY_SIZE);
  }

  public static AsymmetricSecurityCipher of(String location, String password, String keyStoreType) {
    return new AsymmetricSecurityCipher(
        DEFAULT_ALGORITHM,
        Optional.ofNullable(location).orElse(DEFAULT_LOCATION),
        Optional.ofNullable(password).orElse(DEFAULT_PASSWORD),
        Optional.ofNullable(keyStoreType).orElse(DEFAULT_KEY_STORE_TYPE),
        DEFAULT_KEY_SIZE);
  }

  public static AsymmetricSecurityCipher of(
      String location, String password, String keyStoreType, String algorithm) {
    return new AsymmetricSecurityCipher(
        Optional.ofNullable(algorithm).orElse(DEFAULT_ALGORITHM),
        Optional.ofNullable(location).orElse(DEFAULT_LOCATION),
        Optional.ofNullable(password).orElse(DEFAULT_PASSWORD),
        Optional.ofNullable(keyStoreType).orElse(DEFAULT_KEY_STORE_TYPE),
        DEFAULT_KEY_SIZE);
  }

  @Override
  public String encrypt(String content) {
    try {
      byte[] bytes = content.getBytes(StandardCharsets.UTF_8);
      PublicKey publicKey = getPublicKeyFromP12();
      Cipher cipher = Cipher.getInstance(publicKey.getAlgorithm());
      cipher.init(Cipher.ENCRYPT_MODE, publicKey);
      return Base64.getEncoder().encodeToString(cipher.doFinal(bytes));
    } catch (Exception e) {
      logger.error("encrypt with error! ", e);
      return null;
    }
  }

  @Override
  public String decrypt(String content) {
    try {
      byte[] decode = Base64.getDecoder().decode(content.getBytes(StandardCharsets.UTF_8));
      PrivateKey privateKey = getPrivateKeyFromP12();
      Cipher cipher = Cipher.getInstance(privateKey.getAlgorithm());
      cipher.init(Cipher.DECRYPT_MODE, privateKey);
      return new String(cipher.doFinal(decode), StandardCharsets.UTF_8);
    } catch (Exception e) {
      logger.error("decrypt with error! ", e);
      return null;
    }
  }

  public Map<Integer, String> generateKeyPair() {
    try {
      Map<Integer, String> keyMap = new HashMap<>(2);
      // KeyPairGenerator类用于生成公钥和私钥对，基于RSA算法生成对象
      KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance(algorithm);
      // 初始化密钥对生成器，密钥大小为96-1024位
      keyPairGen.initialize(keySize, new SecureRandom());
      // 成一个密钥对，保存在keyPair中
      KeyPair keyPair = keyPairGen.generateKeyPair();
      // 得到私钥
      PrivateKey privateKey = keyPair.getPrivate();
      // 得到公钥
      PublicKey publicKey = keyPair.getPublic();
      // 得到私钥字符串
      // 将公钥和私钥保存到Map
      // 0表示公钥 1表示私钥
      String publicKeyString = new String(Base64.getEncoder().encode(publicKey.getEncoded()));
      String privateKeyString = new String(Base64.getEncoder().encode((privateKey.getEncoded())));
      keyMap.put(0, publicKeyString);
      keyMap.put(1, privateKeyString);
      return keyMap;
    } catch (NoSuchAlgorithmException e) {
      logger.error("generate key pair with error! ", e);
      return null;
    }
  }

  /**
   * PublicKey 转 String
   *
   * @param publicKey public key
   * @return public key string
   */
  public String getPublicKeyString(PublicKey publicKey) {
    return new String(Base64.getEncoder().encode(publicKey.getEncoded()));
  }

  /**
   * PrivateKey 转 String
   *
   * @param privateKey private key
   * @return private key string
   */
  public String getPrivateKeyString(PrivateKey privateKey) {
    return new String(Base64.getEncoder().encode(privateKey.getEncoded()));
  }

  /**
   * String 转 PublicKey
   *
   * @param publicKey public key string
   * @param algorithm algorithm
   * @return public key
   */
  public PublicKey getPublicKey(String publicKey, String algorithm) {
    try {
      byte[] keyBytes = Base64.getDecoder().decode(publicKey);
      X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
      KeyFactory keyFactory = KeyFactory.getInstance(algorithm);
      return keyFactory.generatePublic(keySpec);
    } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
      logger.error("convert string to public key with error! ", e);
      return null;
    }
  }

  /**
   * String 转私钥 PrivateKey
   *
   * @param privateKey private key string
   * @param algorithm algorithm
   * @return private key
   */
  public PrivateKey getPrivateKey(String privateKey, String algorithm) {
    try {
      byte[] keyBytes = Base64.getDecoder().decode(privateKey);
      PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
      KeyFactory keyFactory = KeyFactory.getInstance(algorithm);
      return keyFactory.generatePrivate(keySpec);
    } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
      logger.error("convert string to private key with error! ", e);
      return null;
    }
  }

  public PublicKey getPublicKeyFromP12() {
    try {
      KeyStore keyStore = instance(location, password, keyStoreType);
      Enumeration<String> aliases = keyStore.aliases();
      String alias = "";
      while (aliases.hasMoreElements()) {
        alias = aliases.nextElement();
      }
      Certificate cert = keyStore.getCertificate(alias);
      return cert.getPublicKey();
    } catch (Exception e) {
      logger.error("get public key from p12 with error! ", e);
      return null;
    }
  }

  public PrivateKey getPrivateKeyFromP12() {
    try {
      KeyStore keyStore = instance(location, password, keyStoreType);
      Enumeration<String> aliases = keyStore.aliases();
      String alias = "";
      while (aliases.hasMoreElements()) {
        alias = aliases.nextElement();
      }
      return (PrivateKey) keyStore.getKey(alias, password.toCharArray());
    } catch (Exception e) {
      logger.error("get private key from p12 with error! ", e);
      return null;
    }
  }

  private KeyStore instance(String path, String password, String keyStoreType)
      throws Exception {
    KeyStore keyStore = KeyStore.getInstance(keyStoreType);
    keyStore.load(
        Thread.currentThread().getContextClassLoader().getResourceAsStream(path),
        password.toCharArray());
    return keyStore;
  }
}
