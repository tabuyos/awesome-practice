/*
 * Copyright (c) 2018-2022 Tabuyos All Right Reserved.
 */
package com.tabuyos.redis.quickstart.controller;

import com.tabuyos.redis.quickstart.util.RedisUtil;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * RedisController
 *
 * @author tabuyos
 * @since 2022/4/11
 */
@RestController
@RequestMapping("redis")
public class RedisController {

  private final RedisUtil redisUtil;

  public RedisController(RedisUtil redisUtil) {
    this.redisUtil = redisUtil;
  }

  @GetMapping("get/{key}")
  public String get(@PathVariable("key") String key) {
    return redisUtil.get(key).toString();
  }

  @GetMapping("set/{key}/{value}")
  public boolean set(@PathVariable("key") String key, @PathVariable("value") String value) {
    return redisUtil.set(key, value);
  }
}
