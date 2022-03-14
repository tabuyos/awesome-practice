/*
 * Copyright (c) 2018-2022 Tabuyos All Right Reserved.
 */
package com.tabuyos.eventbus.impl;

/**
 * PromiseImpl
 *
 * @author tabuyos
 * @since 2022/3/11
 */
public class PromiseImpl<T> extends FutureImpl<T>
  implements Promise<T>, Listener<T> {

    @Override
    public void handle(AsyncResult<T> ar) {
        if (ar.succeeded()) {
            onSuccess(ar.result());
        } else {
            onFailure(ar.cause());
        }
    }

    @Override
    public void onSuccess(T value) {
        tryComplete(value);
    }

    @Override
    public void onFailure(Throwable failure) {
        tryFail(failure);
    }

    @Override
    public Future<T> future() {
        return this;
    }
}
