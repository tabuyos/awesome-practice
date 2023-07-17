/*
 * copyright(c) 2018-2023 tabuyos all right reserved.
 */
package com.tabuyos.dysql.quickstart;
/**
 * FromContext
 *
 * @author tabuyos
 * @since 2023/6/20
 */
@SuppressWarnings({"all"})
public interface FromContext {
  void table(String name);

  void table(SqlBuilder sqlBuilder);

  void tableAs(SqlBuilder sqlBuilder, String alias);

  void tableAs(String name, String alias);

  <T> void table(Class<T> table);

  <T> void tableAs(Class<T> table, String alias);

  void join(String name, String condition);

  void joinAs(String name, String condition, String alias);

  <T, P> void join(Class<T> table, SFunction<T, ?> condL, String op, SFunction<P, ?> condR);

  <T, P> void join(
      Class<T> table, SFunction<T, ?> condL, String op, SFunction<P, ?> condR, String alias);

  void leftJoin(String name, String condition);

  void leftJoin(String name, String condition, String alias);

  <T, P> void leftJoin(Class<T> table, SFunction<T, ?> condL, String op, SFunction<P, ?> condR);

  <T, P> void leftJoin(
      Class<T> table, SFunction<T, ?> condL, String op, SFunction<P, ?> condR, String alias);

  void rightJoin(String name, String condition);

  void rightJoin(String name, String condition, String alias);

  <T, P> void rightJoin(Class<T> table, SFunction<T, ?> condL, String op, SFunction<P, ?> condR);

  <T, P> void rightJoin(
      Class<T> table, SFunction<T, ?> condL, String op, SFunction<P, ?> condR, String alias);
}
