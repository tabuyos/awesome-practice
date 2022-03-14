/*
 * Copyright (c) 2018-2022 Tabuyos All Right Reserved.
 */
package com.tabuyos.eventbus.quickstart;

import com.google.common.eventbus.Subscribe;

/**
 * EventListener
 *
 * @author tabuyos
 * @since 2022/3/8
 */
public class EventListener {

  /**
   * 监听 Integer 类型的消息
   */
  @Subscribe
  public void listenInteger(Integer param) {
    System.out.println("EventListener#listenInteger ->" + param);
  }

  /**
   * 监听 String 类型的消息
   */
  @Subscribe
  public void listenString(String param) {
    System.out.println("EventListener#listenString ->" + param);
  }
}
