/*
 * copyright(c) 2018-2023 tabuyos all right reserved.
 */
package com.tabuyos.dysql.quickstart;

import java.io.Serializable;
import java.util.List;
import java.util.function.Supplier;

/**
 * SelectContext
 *
 * @author tabuyos
 * @since 2023/6/20
 */
@SuppressWarnings({"all"})
public interface SelectContext {
  void col(String col);

  <A> void colAs(String col, SFunction<A, ?> alias);

  void col(SqlBuilder sqlBuilder);

  <A> void colAs(SqlBuilder sqlBuilder, SFunction<A, ?> alias);

  void col(List<? extends SFunction<?, ?>> cols);

  void colAs(List<? extends SFunction<?, ?>> cols, List<? extends SFunction<?, ?>> alias);

  <T> void col(SFunction<T, ?> col);

  <T, A> void colAs(SFunction<T, ?> col, SFunction<A, ?> alias);

  void fun(String fun);

  <A> void funAs(String fun, SFunction<A, ?> alias);

  <T> void fun(Supplier<String> fun, SFunction<T, ?> param0);

  <T, A> void funAs(Supplier<String> fun, SFunction<T, ?> param0, SFunction<A, ?> alias);

  <T0, T1> void fun(Supplier<String> fun, SFunction<T0, ?> param0, SFunction<T1, ?> param1);

  <T0, T1, A> void funAs(
      Supplier<String> fun,
      SFunction<T0, ?> param0,
      SFunction<T1, ?> param1,
      SFunction<A, ?> alias);

  <T0, T1, T2> void fun(
      Supplier<String> fun,
      SFunction<T0, ?> param0,
      SFunction<T1, ?> param1,
      SFunction<T2, ?> param2);

  <T0, T1, T2, A> void funAs(
      Supplier<String> fun,
      SFunction<T0, ?> param0,
      SFunction<T1, ?> param1,
      SFunction<T2, ?> param2,
      SFunction<A, ?> alias);

  <T0, T1, T2, T3> void fun(
      Supplier<String> fun,
      SFunction<T0, ?> param0,
      SFunction<T1, ?> param1,
      SFunction<T2, ?> param2,
      SFunction<T3, ?> param3);

  <T0, T1, T2, T3, A> void funAs(
      Supplier<String> fun,
      SFunction<T0, ?> param0,
      SFunction<T1, ?> param1,
      SFunction<T2, ?> param2,
      SFunction<T3, ?> param3,
      SFunction<A, ?> alias);

  <T0, T1, T2, T3, T4> void fun(
      Supplier<String> fun,
      SFunction<T0, ?> param0,
      SFunction<T1, ?> param1,
      SFunction<T2, ?> param2,
      SFunction<T3, ?> param3,
      SFunction<T4, ?> param4);

  <T0, T1, T2, T3, T4, A> void funAs(
      Supplier<String> fun,
      SFunction<T0, ?> param0,
      SFunction<T1, ?> param1,
      SFunction<T2, ?> param2,
      SFunction<T3, ?> param3,
      SFunction<T4, ?> param4,
      SFunction<A, ?> alias);

  void fun(Supplier<String> fun, Supplier<List<? extends SFunction<?, ?>>> paramSupplier);

  <A> void funAs(
      Supplier<String> fun, Supplier<List<? extends SFunction<?, ?>>> paramSupplier, SFunction<A, ?> alias);

  StringBuilder selectSql();
}
