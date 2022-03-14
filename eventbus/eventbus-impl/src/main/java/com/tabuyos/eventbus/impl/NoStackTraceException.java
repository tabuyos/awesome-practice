/*
 * Copyright (c) 2018-2022 Tabuyos All Right Reserved.
 */
package com.tabuyos.eventbus.impl;

/**
 * NoStackTraceException
 *
 * @author tabuyos
 * @since 2022/3/11
 */
public class NoStackTraceException extends Throwable {

    public NoStackTraceException(String message) {
        super(message, null, false, false);
    }
}
