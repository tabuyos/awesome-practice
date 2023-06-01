/*
 * copyright(c) 2018-2023 tabuyos all right reserved.
 */
package com.tabuyos.dysql.quickstart;
/**
 * SqlBuilder
 *
 * @author tabuyos
 * @since 2023/5/31
 */
@FunctionalInterface
public interface SqlBuilder {
  void dosql();

  default void select(String field) {
    System.out.println(field);
  }
}
