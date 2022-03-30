/*
 * Copyright (c) 2018-2021 Tabuyos All Right Reserved.
 */
package com.tabuyos.cipher.encrypt;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Signature;
import java.security.cert.Certificate;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * Encrypt
 *
 * @author tabuyos
 * @since 2021/12/15
 */
public class Encrypt {
  private static final Map<Integer, String> KEY_MAP = new HashMap<>();
  private static final String SHA256_WITH_RSA = "SHA256withRSA";
  private static final String RSA = "RSA";
  private static final String PKCS_PATH = "pkcs-1.p12";
  private static final String PASSWORD = "tabuyos-pkcs12";

  private static final String ALGORITHM = "AES";
  private static final Integer KEY_LENGTH = 256;
  private static final String CHARSET = "utf-8";
  private static final String KEY = "key12345678";

  public static void main(String[] args) throws Exception {
    long b = System.currentTimeMillis();
    String message = "you had me at hello, Tabuyos say hello to you.";
    String encrypt = encrypt(message, getPublicKeyFromP12(PKCS_PATH, PASSWORD));
    System.out.println(encrypt.length());
    System.out.println(encrypt);
    System.out.println(getPublicKeyFromP12(PKCS_PATH, PASSWORD));
    System.out.println("-----------------------------------------------------");
    String decrypt = decrypt(encrypt, getPrivateKeyFromP12(PKCS_PATH, PASSWORD));
    System.out.println(decrypt.length());
    System.out.println(decrypt);
    System.out.println(decrypt.equals(message));
    System.out.println("-----------------------------------------------------");
    String sign = sign(message, getPrivateKey(getPrivateKeyFromP12(PKCS_PATH, PASSWORD)));
    System.out.println(sign);
    System.out.println("-----------------------------------------------------");
    boolean verify = verify(message, sign, getPublicKey(getPublicKeyFromP12(PKCS_PATH, PASSWORD)));
    System.out.println(verify);
    System.out.println("-----------------------------------------------------");
    long a = System.currentTimeMillis();
    System.out.println(a - b);
    SecretKeySpec keySpec = generateKey(KEY);
    String content = "我爱你";
    //
    // String encrypt = encrypt1(content, keySpec);
    // System.out.println(encrypt);
    //
    // String decrypt = decrypt1(encrypt, keySpec);
    // System.out.println(decrypt);
  }

  private static SecretKeySpec generateKey(String password) throws Exception {
    // 创建AES的Key生产者
    KeyGenerator kgen = KeyGenerator.getInstance(ALGORITHM);
    // 利用用户密码作为随机数初始化出
    // SecureRandom是生成安全随机数序列，password.getBytes()是种子，只要种子相同，序列就一样，所以解密只要有password就行
    kgen.init(KEY_LENGTH, new SecureRandom(password.getBytes()));
    // 根据用户密码，生成一个密钥
    SecretKey secretKey = kgen.generateKey();
    // 返回基本编码格式的密钥，如果此密钥不支持编码，则返回
    byte[] enCodeFormat = secretKey.getEncoded();
    // 转换为AES专用密钥
    SecretKeySpec key = new SecretKeySpec(enCodeFormat, ALGORITHM);
    return key;
  }

  /**
   * AES加密字符串
   *
   * @param content 需要被加密的字符串
   * @param key 加密需要的密码
   * @return 密文
   */
  public static String encrypt1(String content, SecretKeySpec key) {
    try {
      //      SecretKeySpec key = generateKey(password);
      // 创建密码器
      Cipher cipher = Cipher.getInstance(ALGORITHM);
      byte[] byteContent = content.getBytes(CHARSET);
      // 初始化为加密模式的密码器
      cipher.init(Cipher.ENCRYPT_MODE, key);
      // 加密
      byte[] result = cipher.doFinal(byteContent);
      // 二进制转换成16进制字符串

      return Base64.getEncoder().encodeToString(result);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  /**
   * 解密AES加密过的字符串
   *
   * @param content AES加密过过的内容
   * @param key 加密时的密码
   * @return 明文
   */
  public static String decrypt1(String content, SecretKeySpec key) {
    try {
      // 十六进制字符串转换成二进制字节数组
      byte[] byteArr = Base64.getDecoder().decode(content.getBytes(StandardCharsets.UTF_8));

      //      SecretKeySpec key = generateKey(password);
      Cipher cipher = Cipher.getInstance(ALGORITHM);
      cipher.init(Cipher.DECRYPT_MODE, key);
      // 解密
      byte[] result = cipher.doFinal(byteArr);

      return new String(result, CHARSET);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  /**
   * 将二进制转换成16进制
   *
   * @param buf
   * @return
   */
  public static String parseByte2HexStr(byte buf[]) {
    StringBuffer sb = new StringBuffer();
    for (int i = 0; i < buf.length; i++) {
      String hex = Integer.toHexString(buf[i] & 0xFF);
      if (hex.length() == 1) {
        hex = '0' + hex;
      }
      sb.append(hex.toUpperCase());
    }
    return sb.toString();
  }

  /**
   * 将16进制转换为二进制
   *
   * @param hexStr
   * @return
   */
  public static byte[] parseHexStr2Byte(String hexStr) {
    if (hexStr.length() < 1) {
      return null;
    }
    byte[] result = new byte[hexStr.length() / 2];
    for (int i = 0; i < hexStr.length() / 2; i++) {
      int high = Integer.parseInt(hexStr.substring(i * 2, i * 2 + 1), 16);
      int low = Integer.parseInt(hexStr.substring(i * 2 + 1, i * 2 + 2), 16);
      result[i] = (byte) (high * 16 + low);
    }
    return result;
  }

  public static void genKeyPair() throws NoSuchAlgorithmException {
    // KeyPairGenerator类用于生成公钥和私钥对，基于RSA算法生成对象
    KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance(RSA);
    // 初始化密钥对生成器，密钥大小为96-1024位
    //    keyPairGen.initialize(1024, new SecureRandom());
    keyPairGen.initialize(2048, new SecureRandom());
    // 成一个密钥对，保存在keyPair中
    KeyPair keyPair = keyPairGen.generateKeyPair();
    // 得到私钥
    //    PrivateKey privateKey = keyPair.getPrivate();
    RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
    // 得到公钥
    //    PublicKey publicKey = keyPair.getPublic();
    RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
    // 得到私钥字符串
    // 将公钥和私钥保存到Map
    // 0表示公钥 1表示私钥
    String publicKeyString = new String(Base64.getEncoder().encode(publicKey.getEncoded()));
    String privateKeyString = new String(Base64.getEncoder().encode((privateKey.getEncoded())));
    KEY_MAP.put(0, publicKeyString);
    KEY_MAP.put(1, privateKeyString);
  }

  /**
   * RSA公钥加密
   *
   * @param content 加密字符串
   * @param publicKey 公钥
   * @return 密文
   * @throws Exception 加密过程中的异常信息
   */
  public static String encrypt(String content, String publicKey) throws Exception {
    byte[] bytes = content.getBytes(StandardCharsets.UTF_8);
    RSAPublicKey pubKey = (RSAPublicKey) getPublicKey(publicKey);
    Cipher cipher = Cipher.getInstance(RSA);
    cipher.init(Cipher.ENCRYPT_MODE, pubKey);
    return Base64.getEncoder().encodeToString(cipher.doFinal(bytes));
  }

  /**
   * RSA私钥解密
   *
   * @param content 加密字符串
   * @param privateKey 私钥
   * @return 铭文
   * @throws Exception 解密过程中的异常信息
   */
  public static String decrypt(String content, String privateKey) throws Exception {
    byte[] decode = Base64.getDecoder().decode(content.getBytes(StandardCharsets.UTF_8));
    RSAPrivateKey priKey = (RSAPrivateKey) getPrivateKey(privateKey);
    Cipher cipher = Cipher.getInstance(RSA);
    cipher.init(Cipher.DECRYPT_MODE, priKey);
    return new String(cipher.doFinal(decode), StandardCharsets.UTF_8);
  }

  /**
   * use private key sign content
   *
   * @param content content
   * @param privateKey private key
   * @return signature
   * @throws Exception ex
   */
  public static String sign(String content, PrivateKey privateKey) throws Exception {
    Signature privateSignature = Signature.getInstance(SHA256_WITH_RSA);
    privateSignature.initSign(privateKey);
    privateSignature.update(content.getBytes(StandardCharsets.UTF_8));
    byte[] signature = privateSignature.sign();
    return Base64.getEncoder().encodeToString(signature);
  }

  /**
   * use public key verify content
   *
   * @param content content
   * @param signature signature
   * @param publicKey public key
   * @return result of verify
   * @throws Exception ex
   */
  public static boolean verify(String content, String signature, PublicKey publicKey)
      throws Exception {
    byte[] decode = Base64.getDecoder().decode(signature);
    Signature publicSignature = Signature.getInstance(SHA256_WITH_RSA);
    publicSignature.initVerify(publicKey);
    publicSignature.update(content.getBytes(StandardCharsets.UTF_8));
    return publicSignature.verify(decode);
  }

  /**
   * String 转私钥 PrivateKey
   *
   * @param publicKey public key string
   * @return public key
   * @throws Exception ex
   */
  public static PublicKey getPublicKey(String publicKey) throws Exception {
    byte[] keyBytes = Base64.getDecoder().decode(publicKey);
    X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
    KeyFactory keyFactory = KeyFactory.getInstance(RSA);
    return keyFactory.generatePublic(keySpec);
  }

  /**
   * String 转私钥 PrivateKey
   *
   * @param privateKey private key string
   * @return private key
   * @throws Exception ex
   */
  public static PrivateKey getPrivateKey(String privateKey) throws Exception {
    byte[] keyBytes = Base64.getDecoder().decode(privateKey);
    PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
    KeyFactory keyFactory = KeyFactory.getInstance(RSA);
    return keyFactory.generatePrivate(keySpec);
  }

  public static byte[] getBytes(String content) {
    return Base64.getDecoder().decode(content.getBytes(StandardCharsets.UTF_8));
  }

  public static byte[] getBytes(byte[] content) {
    return Base64.getDecoder().decode(content);
  }

  public static String getPublicKeyFromP12(String path, String password) throws Exception {
    KeyStore keyStore = instance(path, password);
    Enumeration<String> aliases = keyStore.aliases();
    String alias = "";
    while (aliases.hasMoreElements()) {
      alias = aliases.nextElement();
    }
    Certificate cert = keyStore.getCertificate(alias);
    return new String(Base64.getEncoder().encode(cert.getPublicKey().getEncoded()));
  }

  public static String getPrivateKeyFromP12(String path, String password) throws Exception {
    KeyStore keyStore = instance(path, password);
    Enumeration<String> aliases = keyStore.aliases();
    String alias = "";
    while (aliases.hasMoreElements()) {
      alias = aliases.nextElement();
    }
    System.out.println(alias);
    RSAPrivateKey privateKey = (RSAPrivateKey) keyStore.getKey(alias, password.toCharArray());
    return new String(Base64.getEncoder().encode(privateKey.getEncoded()));
  }

  private static KeyStore instance(String path, String password) throws Exception {
    KeyStore keyStore = KeyStore.getInstance("PKCS12");
    keyStore.load(
        Thread.currentThread().getContextClassLoader().getResourceAsStream(path),
        password.toCharArray());
    return keyStore;
  }
}
