/*
 * Copyright (c) 2018-2021 Tabuyos All Right Reserved.
 */
package com.tabuyos.authserver.controller;

import com.tabuyos.authserver.entity.Reply;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * AuthenticationServerController
 *
 * @author tabuyos
 * @since 2021/12/10
 */
@SuppressWarnings("DuplicatedCode")
@RestController
@RequestMapping("auth")
public class AuthenticationServerController {

  @PostMapping("code")
  public Reply code() {
    Map<String, String> map = new HashMap<>(7);
    map.put("access_token", "111");
    map.put("refresh_token", "222");
    Reply reply = new Reply();
    reply.setCode(200);
    reply.setState(Boolean.TRUE);
    reply.setMessage("/auth/code api");
    reply.setData(map);
    return reply;
  }

  @PostMapping("token")
  public Reply token() {
    Map<String, String> map = new HashMap<>(7);
    map.put("access_token", "111");
    map.put("refresh_token", "222");
    Reply reply = new Reply();
    reply.setCode(200);
    reply.setState(Boolean.TRUE);
    reply.setMessage("/auth/token api");
    reply.setData(map);
    return reply;
  }
}
