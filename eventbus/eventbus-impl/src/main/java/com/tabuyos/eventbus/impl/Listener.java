/*
 * Copyright (c) 2018-2022 Tabuyos All Right Reserved.
 */
package com.tabuyos.eventbus.impl;

/**
 * Listener
 *
 * @author tabuyos
 * @since 2022/3/11
 */
public interface Listener<T> {


    /**
     * Signal the success.
     *
     * @param value the value
     */
    void onSuccess(T value);

    /**
     * Signal the failure
     *
     * @param failure the failure
     */
    void onFailure(Throwable failure);
}
