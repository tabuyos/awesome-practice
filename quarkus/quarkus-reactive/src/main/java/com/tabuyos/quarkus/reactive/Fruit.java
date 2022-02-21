/*
 * Copyright (c) 2018-2022 Tabuyos All Right Reserved.
 */
package com.tabuyos.quarkus.reactive;

import io.quarkus.hibernate.reactive.panache.PanacheEntity;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;

/**
 * Fruit
 *
 * @author tabuyos
 * @since 2022/2/16
 */
@Entity
@Cacheable
public class Fruit extends PanacheEntity {

  @Column(length = 40, unique = true)
  public String name;
}
