/*
 * Copyright (c) 2018-2022 Tabuyos All Right Reserved.
 */
package com.tabuyos.apollo.test.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * NameController
 *
 * @author tabuyos
 * @since 2022/1/5
 */
@RestController
public class NameController {

  @Value(value = "${mxn.name:hacking-tech}")
  private String name;

  @GetMapping("getMxnName")
  public String getMxnName() {
    return "hello, " + name;
  }
}
