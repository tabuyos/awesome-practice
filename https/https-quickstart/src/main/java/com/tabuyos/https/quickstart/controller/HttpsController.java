/*
 * Copyright (c) 2018-2022 Tabuyos All Right Reserved.
 */
package com.tabuyos.https.quickstart.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * HttpsController
 *
 * @author tabuyos
 * @since 2022/3/29
 */
@RestController
@RequestMapping("https")
public class HttpsController {

  @GetMapping("hello")
  @PostMapping("hello")
  public String hello() {
    // try {
    //   runtime();
    //   exception();
    // } catch (Exception e) {
    //   System.out.println("----------------------");
    //   System.out.println(e.getMessage());
    //   System.out.println("----------------------");
    //   e.printStackTrace();
    // }
    return "hello https";
  }

  private void runtime() {
    throw new RuntimeException("runtime exception");
  }

  private void exception() throws IllegalAccessException {
    throw new IllegalAccessException("illegal access exception");
  }
}
