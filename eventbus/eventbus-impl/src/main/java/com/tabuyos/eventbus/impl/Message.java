/*
 * Copyright (c) 2018-2022 Tabuyos All Right Reserved.
 */
package com.tabuyos.eventbus.impl;

import java.util.ArrayList;
import java.util.List;

/**
 * Message
 *
 * @author tabuyos
 * @since 2022/3/8
 */
public class Message {

    private final transient List<String> names;
    private final transient String address;
    private final transient String body;

    private Message(List<String> names, String address, String body) {
        this.names = names;
        this.address = address;
        this.body = body;
    }

    public static Message of(List<String> names) {
        return of(names, null, null);
    }

    public static Message of(String body) {
        return of(new ArrayList<>(), null, body);
    }

    public static Message of(String address, String body) {
        return of(new ArrayList<>(), address, body);
    }

    public static Message of(List<String> names, String address, String body) {
        return new Message(names, address, body);
    }

    public List<String> names() {
        return names;
    }

    public String address() {
        return address;
    }

    public String body() {
        return body;
    }
}
