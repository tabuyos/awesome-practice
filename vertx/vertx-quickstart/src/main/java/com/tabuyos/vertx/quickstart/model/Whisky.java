/*
 * Copyright (c) 2018-2022 Tabuyos All Right Reserved.
 */
package com.tabuyos.vertx.quickstart.model;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Whisky
 *
 * @author tabuyos
 * @since 2022/2/17
 */
public class Whisky {

  private static final AtomicInteger COUNTER = new AtomicInteger();

  private final int id;

  private String name;

  private String origin;

  public Whisky(String name, String origin) {
    this.id = COUNTER.getAndIncrement();
    this.name = name;
    this.origin = origin;
  }

  public Whisky() {
    this.id = COUNTER.getAndIncrement();
  }

  public String getName() {
    return name;
  }

  public String getOrigin() {
    return origin;
  }

  public int getId() {
    return id;
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setOrigin(String origin) {
    this.origin = origin;
  }
}
