/*
 * Copyright (c) 2018-2022 Tabuyos All Right Reserved.
 */
package com.tabuyos.eventbus.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * SubscriberRegistry
 *
 * @author tabuyos
 * @since 2022/3/8
 */
public enum SubscriberRegistry {

    /**
     * instance for event.
     */
    INSTANCE;

    private final Map<String, List<Handler<Message>>> eventMap;
    private final ThreadFactory factory = (runnable) ->
        new Thread(runnable,
                   "event-pool-" + runnable.hashCode());
    private final ThreadPoolExecutor eventPool =
        new ThreadPoolExecutor(1,
                               5,
                               0L,
                               TimeUnit.MILLISECONDS,
                               new LinkedBlockingQueue<>(),
                               factory);

    /**
     * 构造函数
     */
    SubscriberRegistry() {
        eventMap = new ConcurrentHashMap<>();
    }

    /**
     * 注册
     *
     * @param address 地址
     * @param handler 处理器
     */
    public void register(String address, Handler<Message> handler) {
        List<Handler<Message>> events = eventMap.getOrDefault(address, new ArrayList<>());
        events.add(handler);
        eventMap.putIfAbsent(address, events);
    }

    /**
     * 注销
     *
     * @param address 地址
     */
    public void unregister(String address) {
        eventMap.remove(address);
    }

    public List<Handler<Message>> subscriber(String address) {
        return eventMap.getOrDefault(address, Collections.emptyList());
    }

    public Future<String> publish(String address, Message message) {
        Promise<String> promise = Promise.promise();
        eventPool.execute(() -> {
            subscriber(address).forEach(handler -> handler.handle(message));
            promise.complete(address);
        });
        return promise.future();
    }
}
