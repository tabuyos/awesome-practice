/*
 * Copyright (c) 2018-2022 Tabuyos All Right Reserved.
 */
package com.tabuyos.eventbus.impl;

/**
 * AsyncResult
 *
 * @author tabuyos
 * @since 2022/3/11
 */
public interface AsyncResult<T> {

    /**
     * result
     *
     * @return T
     */
    T result();

    /**
     * cause
     *
     * @return throwable
     */
    Throwable cause();

    /**
     * succeeded
     *
     * @return boolean
     */
    boolean succeeded();

    /**
     * failed
     *
     * @return boolean
     */
    boolean failed();
}
