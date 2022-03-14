/*
 * Copyright (c) 2018-2022 Tabuyos All Right Reserved.
 */
package com.tabuyos.eventbus.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * EventBus
 * <p>
 * 实现一个最简单的事件总线
 *
 * @author tabuyos
 * @since 2022/3/8
 */
public class EventBus {

    private static final Logger logger = LoggerFactory.getLogger(EventBus.class);

    private final SubscriberRegistry subscriberRegistry = SubscriberRegistry.INSTANCE;

    public void register(String address, Handler<Message> handler) {
        logger.info("register event address: " + address);
        subscriberRegistry.register(address, handler);
    }

    public void unregister(String address) {
        logger.info("unregister event address: " + address);
        subscriberRegistry.unregister(address);
    }

    public Future<String> publish(String address, Message message) {
        logger.info("publish event address: " + address);
        return subscriberRegistry.publish(address, message);
    }
}
