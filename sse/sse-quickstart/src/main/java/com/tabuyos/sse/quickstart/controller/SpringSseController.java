/*
 * Copyright (c) 2018-2022 Tabuyos All Right Reserved.
 */
package com.tabuyos.sse.quickstart.controller;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * SpringSSEController
 *
 * @author tabuyos
 * @since 2022/3/24
 */
@RestController
@RequestMapping("spring-sse")
@CrossOrigin(origins = {"http://localhost:8080"}, allowCredentials = "true")
public class SpringSseController {

  private static final Map<String, SseEmitter> SSE_CACHE = new ConcurrentHashMap<>();

  @ResponseBody
  @GetMapping(path = "subscribe", produces = {MediaType.TEXT_EVENT_STREAM_VALUE})
  public SseEmitter subscribe(String id) {
    // 超时时间设置为1小时
    SseEmitter sseEmitter = new SseEmitter(3600_000L);
    SSE_CACHE.put(id, sseEmitter);
    sseEmitter.onTimeout(() -> SSE_CACHE.remove(id));
    sseEmitter.onCompletion(() -> System.out.println("完成！！！"));
    return sseEmitter;
  }

  @ResponseBody
  @GetMapping("push")
  public String push(String id, String content) throws IOException {
    SseEmitter sseEmitter = SSE_CACHE.get(id);
    if (sseEmitter != null) {
      SseEmitter.SseEventBuilder eventBuilder = SseEmitter.event()
                                                          .data(content)
                                                          .name("tabuyos")
                                                          .reconnectTime(5000)
                                                          .comment("comment")
                                                          .id("p1");
      sseEmitter.send(eventBuilder);
    }
    return "over";
  }

  @ResponseBody
  @GetMapping("over")
  public String over(String id) {
    SseEmitter sseEmitter = SSE_CACHE.get(id);
    if (sseEmitter != null) {
      sseEmitter.complete();
      SSE_CACHE.remove(id);
    }
    return "over";
  }
}
