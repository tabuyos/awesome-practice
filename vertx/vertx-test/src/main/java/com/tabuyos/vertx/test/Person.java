/*
 * Copyright (c) 2018-2022 Tabuyos All Right Reserved.
 */
package com.tabuyos.vertx.test;

/**
 * Person
 *
 * @author tabuyos
 * @since 2022/3/4
 */
public interface Person<T extends A> {

  void accept(T t);
}
