/*
 * Copyright (c) 2018-2022 Tabuyos All Right Reserved.
 */
package com.tabuyos.eventbus.quickstart;

import com.google.common.eventbus.AsyncEventBus;
import com.google.common.eventbus.EventBus;

import java.util.concurrent.Executors;

/**
 * Tabuyos
 *
 * @author tabuyos
 * @since 2022/3/8
 */
@SuppressWarnings("AlibabaThreadPoolCreation")
public class Tabuyos {

  public static void main(String[] args) {
    EventBus eventBus = new AsyncEventBus(Executors.newCachedThreadPool(), (exception, context) -> {
      exception.printStackTrace();
      System.out.println(context);
    });
    EventListener eventListener = new EventListener();
    CustomEventListener customEventListener = new CustomEventListener();
    eventBus.register(eventListener);
    eventBus.register(customEventListener);
    eventBus.post(1);
    eventBus.post(2);
    eventBus.unregister(eventListener);
    eventBus.post("3");
  }
}
