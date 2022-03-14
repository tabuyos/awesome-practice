/*
 * Copyright (c) 2018-2022 Tabuyos All Right Reserved.
 */
package com.tabuyos.eventbus.impl;

/**
 * Handler
 *
 * @author tabuyos
 * @since 2022/3/8
 */
@FunctionalInterface
public interface Handler<E> {

    /**
     * Something has happened, so handle it.
     *
     * @param event  the event to handle
     */
    void handle(E event);
}
