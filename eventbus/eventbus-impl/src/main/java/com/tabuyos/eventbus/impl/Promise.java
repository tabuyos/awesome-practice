/*
 * Copyright (c) 2018-2022 Tabuyos All Right Reserved.
 */
package com.tabuyos.eventbus.impl;

/**
 * Promise
 *
 * @author tabuyos
 * @since 2022/3/11
 */
@SuppressWarnings("unused")
public interface Promise<T> extends Handler<AsyncResult<T>> {

    /**
     * promise
     *
     * @param <T> T
     * @return T
     */
    static <T> Promise<T> promise() {
        return new PromiseImpl<>();
    }

    /**
     * handle
     *
     * @param asyncResult 异步结果
     */
    @Override
    default void handle(AsyncResult<T> asyncResult) {
        if (asyncResult.succeeded()) {
            complete(asyncResult.result());
        } else {
            fail(asyncResult.cause());
        }
    }

    /**
     * complete
     *
     * @param result result
     */
    default void complete(T result) {
        if (!tryComplete(result)) {
            throw new IllegalStateException("Result is already complete");
        }
    }

    /**
     * complete
     */
    default void complete() {
        if (!tryComplete()) {
            throw new IllegalStateException("Result is already complete");
        }
    }

    /**
     * fail
     *
     * @param cause thr
     */
    default void fail(Throwable cause) {
        if (!tryFail(cause)) {
            throw new IllegalStateException("Result is already complete");
        }
    }

    /**
     * fail
     *
     * @param message message
     */
    default void fail(String message) {
        if (!tryFail(message)) {
            throw new IllegalStateException("Result is already complete");
        }
    }

    /**
     * try to complete
     *
     * @param result result
     * @return true if complete
     */
    boolean tryComplete(T result);

    /**
     * try to complete
     *
     * @return result
     */
    default boolean tryComplete() {
        return tryComplete(null);
    }

    /**
     * try fail
     *
     * @param cause cause
     * @return true if fail
     */
    boolean tryFail(Throwable cause);

    /**
     * try fail
     *
     * @param message message
     * @return true if fail
     */
    default boolean tryFail(String message) {
        return tryFail(new NoStackTraceException(message));
    }

    /**
     * future
     *
     * @return future
     */
    Future<T> future();
}
