/*
 * Copyright (c) 2018-2021 Tabuyos All Right Reserved.
 */
package com.tabuyos.cipher.encrypt;

/**
 * SecurityCipher
 *
 * @author tabuyos
 * @since 2021/12/15
 */
public interface SecurityCipher {
  /**
   * 加密
   *
   * @param content 原始内容
   * @return 加密后的内容
   */
  String encrypt(String content);

  /**
   * 解密
   *
   * @param content 加密内容
   * @return 解密后的内容
   */
  String decrypt(String content);
}
