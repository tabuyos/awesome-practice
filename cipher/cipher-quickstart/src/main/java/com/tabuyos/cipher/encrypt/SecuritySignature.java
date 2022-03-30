/*
 * Copyright (c) 2018-2021 Tabuyos All Right Reserved.
 */
package com.tabuyos.cipher.encrypt;

import java.security.PrivateKey;
import java.security.PublicKey;

/**
 * SecuritySignature
 *
 * @author tabuyos
 * @since 2021/12/15
 */
public interface SecuritySignature {

  /**
   * 生成签名
   *
   * @param content 原始内容
   * @return 签名
   */
  String sign(String content);

  /**
   * 验证签名
   *
   * @param content 原始内容
   * @param signature 签名
   * @return 是否通过
   */
  boolean verify(String content, String signature);
}
