/*
 * Copyright (c) 2018-2022 Tabuyos All Right Reserved.
 */
package com.tabuyos.eventbus.impl;

import java.util.ArrayList;
import java.util.Objects;

/**
 * FutureImpl
 *
 * @author tabuyos
 * @since 2022/3/11
 */
@SuppressWarnings({"UnusedReturnValue", "unchecked"})
public class FutureImpl<T> implements Future<T> {

    private static final Object NULL_VALUE = new Object();
    private Object value;
    private Listener<T> listener;

    @Override
    public synchronized boolean isComplete() {
        return value != null;
    }

    @Override
    public Future<T> onComplete(Handler<AsyncResult<T>> handler) {
        Objects.requireNonNull(handler, "No null handler accepted");
        Listener<T> listener;
        if (handler instanceof Listener) {
            listener = (Listener<T>) handler;
        } else {
            listener = new Listener<T>() {
                @Override
                public void onSuccess(T value) {
                    handler.handle(FutureImpl.this);
                }
                @Override
                public void onFailure(Throwable failure) {
                    handler.handle(FutureImpl.this);
                }
            };
        }
        addListener(listener);
        return this;
    }

    public boolean tryComplete(T result) {
        Listener<T> lis;
        synchronized (this) {
            if (value != null) {
                return false;
            }
            value = result == null ? NULL_VALUE : result;
            lis = listener;
            listener = null;
        }
        if (lis != null) {
            emitSuccess(result, lis);
        }
        return true;
    }

    public boolean tryFail(Throwable cause) {
        if (cause == null) {
            cause = new NoStackTraceException(null);
        }
        Listener<T> l;
        synchronized (this) {
            if (value != null) {
                return false;
            }
            value = new Throwable(cause);
            l = listener;
            listener = null;
        }
        if (l != null) {
            emitFailure(cause, l);
        }
        return true;
    }

    @Override
    public synchronized T result() {
        return value instanceof Throwable ? null : value == NULL_VALUE ? null : (T) value;
    }

    @Override
    public Throwable cause() {
        return value instanceof Throwable ? ((Throwable) value).getCause() : null;
    }

    @Override
    public boolean succeeded() {
        return value != null && !(value instanceof Throwable);
    }

    @Override
    public boolean failed() {
        return value instanceof Throwable;
    }

    public void addListener(Listener<T> listener) {
        Object vl;
        synchronized (this) {
            vl = value;
            if (vl == null) {
                if (this.listener == null) {
                    this.listener = listener;
                } else {
                    ListenerArray<T> listeners;
                    if (this.listener instanceof FutureImpl.ListenerArray) {
                        listeners = (ListenerArray<T>) this.listener;
                    } else {
                        listeners = new ListenerArray<>();
                        listeners.add(this.listener);
                        this.listener = listeners;
                    }
                    listeners.add(listener);
                }
                return;
            }
        }
        if (vl instanceof Throwable) {
            emitFailure(((Throwable)vl).getCause(), listener);
        } else {
            if (vl == NULL_VALUE) {
                vl = null;
            }
            emitSuccess((T) vl, listener);
        }
    }

    protected final void emitSuccess(T value, Listener<T> listener) {
        listener.onSuccess(value);
    }

    protected final void emitFailure(Throwable cause, Listener<T> listener) {
        listener.onFailure(cause);
    }

    private static class ListenerArray<T> extends ArrayList<Listener<T>> implements Listener<T> {
        @Override
        public void onSuccess(T value) {
            for (Listener<T> listener : this) {
                listener.onSuccess(value);
            }
        }
        @Override
        public void onFailure(Throwable failure) {
            for (Listener<T> listener : this) {
                listener.onFailure(failure);
            }
        }
    }
}
