/*
 * Copyright (c) 2018-2022 Tabuyos All Right Reserved.
 */
package com.tabuyos.nacos.springboot.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * TestController
 *
 * @author tabuyos
 * @since 2022/1/7
 */
@RestController
@RequestMapping("test")
public class TestController {

  @GetMapping("index")
  public String test() {
    return "test";
  }
}
