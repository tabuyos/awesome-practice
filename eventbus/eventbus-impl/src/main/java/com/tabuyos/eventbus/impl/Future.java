/*
 * Copyright (c) 2018-2022 Tabuyos All Right Reserved.
 */
package com.tabuyos.eventbus.impl;

/**
 * Future
 *
 * @author tabuyos
 * @since 2022/3/11
 */
@SuppressWarnings({"unused", "UnusedReturnValue"})
public interface Future<T> extends AsyncResult<T> {

    /**
     * 是否完成
     *
     * @return boolean
     */
    boolean isComplete();

    /**
     * 完成时
     *
     * @param handler handler
     * @return future
     */
    Future<T> onComplete(Handler<AsyncResult<T>> handler);

    /**
     * 成功时
     *
     * @param handler handler
     * @return future
     */
    default Future<T> onSuccess(Handler<T> handler) {
        return onComplete(ar -> {
            if (ar.succeeded()) {
                handler.handle(ar.result());
            }
        });
    }

    /**
     * 失败时
     *
     * @param handler handler
     * @return future
     */
    default Future<T> onFailure(Handler<Throwable> handler) {
        return onComplete(ar -> {
            if (ar.failed()) {
                handler.handle(ar.cause());
            }
        });
    }

    /**
     * result
     *
     * @return T
     */
    @Override
    T result();

    /**
     * cause
     *
     * @return throwable
     */
    @Override
    Throwable cause();

    /**
     * succeeded
     *
     * @return boolean
     */
    @Override
    boolean succeeded();

    /**
     * failed
     *
     * @return boolean
     */
    @Override
    boolean failed();
}
