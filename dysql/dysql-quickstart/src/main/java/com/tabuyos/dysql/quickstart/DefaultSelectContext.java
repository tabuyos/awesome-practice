/*
 * copyright(c) 2018-2023 tabuyos all right reserved.
 */
package com.tabuyos.dysql.quickstart;

import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.IntStream;

/**
 * DefaultSelectContext
 *
 * @author tabuyos
 * @since 2023/7/17
 */
public class DefaultSelectContext implements SelectContext {

  private final StringBuilder sb = new StringBuilder();

  {
    sb.append("select");
  }

  @Override
  public void col(String col) {
    colAs(col, null);
  }

  @Override
  public <A> void colAs(String col, SFunction<A, ?> alias) {
    sb.append(" ").append(col);
    Optional.ofNullable(alias).map(Lambdas::getName).ifPresent(as -> sb.append(" as ").append(as));
  }

  @Override
  public void col(SqlBuilder sqlBuilder) {
    colAs(sqlBuilder, null);
  }

  @Override
  public <A> void colAs(SqlBuilder sqlBuilder, SFunction<A, ?> alias) {
    sb.append(" ").append(sqlBuilder.sql());
    Optional.ofNullable(alias).map(Lambdas::getName).ifPresent(as -> sb.append(" as ").append(as));
  }

  @Override
  public void col(List<? extends SFunction<?, ?>> cols) {
    colAs(cols, null);
  }

  @Override
  public void colAs(List<? extends SFunction<?, ?>> cols, List<? extends SFunction<?, ?>> alias) {
    IntStream.range(0, cols.size())
        .forEach(
            index -> {
              SFunction<?, ?> sf = cols.get(index);
              sb.append(" ").append(Lambdas.getName(sf));
              Optional.ofNullable(alias)
                  .filter(als -> index < als.size())
                  .map(als -> als.get(index))
                  .map(Lambdas::getName)
                  .ifPresent(as -> sb.append(" as ").append(as));
            });
  }

  @Override
  public <T> void col(SFunction<T, ?> col) {
    colAs(col, null);
  }

  @Override
  public <T, A> void colAs(SFunction<T, ?> col, SFunction<A, ?> alias) {
    sb.append(" ").append(Lambdas.getName(col));
    Optional.ofNullable(alias).map(Lambdas::getName).ifPresent(as -> sb.append(" as ").append(as));
  }

  @Override
  public void fun(String fun) {
    funAs(fun, null);
  }

  @Override
  public <A> void funAs(String fun, SFunction<A, ?> alias) {
    sb.append(" ").append(fun);
    Optional.ofNullable(alias).map(Lambdas::getName).ifPresent(as -> sb.append(" as ").append(as));
  }

  @Override
  public <T> void fun(Supplier<String> fun, SFunction<T, ?> param0) {
    funAs(fun, param0, null);
  }

  @Override
  public <T, A> void funAs(Supplier<String> fun, SFunction<T, ?> param0, SFunction<A, ?> alias) {
    String func = fun.get();
    String para = Lambdas.getName(param0);
    func = func.replace("%0", para);
    sb.append(" ").append(func);
    Optional.ofNullable(alias).map(Lambdas::getName).ifPresent(as -> sb.append(" as ").append(as));
  }

  @Override
  public <T0, T1> void fun(Supplier<String> fun, SFunction<T0, ?> param0, SFunction<T1, ?> param1) {
    funAs(fun, param0, param1, null);
  }

  @Override
  public <T0, T1, A> void funAs(
      Supplier<String> fun,
      SFunction<T0, ?> param0,
      SFunction<T1, ?> param1,
      SFunction<A, ?> alias) {
    String func = fun.get();
    String para0 = Lambdas.getName(param0);
    String para1 = Lambdas.getName(param1);
    func = func.replace("%0", para0);
    func = func.replace("%1", para1);
    sb.append(" ").append(func);
    Optional.ofNullable(alias).map(Lambdas::getName).ifPresent(as -> sb.append(" as ").append(as));
  }

  @Override
  public <T0, T1, T2> void fun(
      Supplier<String> fun,
      SFunction<T0, ?> param0,
      SFunction<T1, ?> param1,
      SFunction<T2, ?> param2) {
    funAs(fun, param0, param1, param2, null);
  }

  @Override
  public <T0, T1, T2, A> void funAs(
      Supplier<String> fun,
      SFunction<T0, ?> param0,
      SFunction<T1, ?> param1,
      SFunction<T2, ?> param2,
      SFunction<A, ?> alias) {
    String func = fun.get();
    String para0 = Lambdas.getName(param0);
    String para1 = Lambdas.getName(param1);
    String para2 = Lambdas.getName(param2);
    func = func.replace("%0", para0);
    func = func.replace("%1", para1);
    func = func.replace("%2", para2);
    sb.append(" ").append(func);
    Optional.ofNullable(alias).map(Lambdas::getName).ifPresent(as -> sb.append(" as ").append(as));
  }

  @Override
  public <T0, T1, T2, T3> void fun(
      Supplier<String> fun,
      SFunction<T0, ?> param0,
      SFunction<T1, ?> param1,
      SFunction<T2, ?> param2,
      SFunction<T3, ?> param3) {
    funAs(fun, param0, param1, param2, param3, null);
  }

  @Override
  public <T0, T1, T2, T3, A> void funAs(
      Supplier<String> fun,
      SFunction<T0, ?> param0,
      SFunction<T1, ?> param1,
      SFunction<T2, ?> param2,
      SFunction<T3, ?> param3,
      SFunction<A, ?> alias) {
    String func = fun.get();
    String para0 = Lambdas.getName(param0);
    String para1 = Lambdas.getName(param1);
    String para2 = Lambdas.getName(param2);
    String para3 = Lambdas.getName(param3);
    func = func.replace("%0", para0);
    func = func.replace("%1", para1);
    func = func.replace("%2", para2);
    func = func.replace("%3", para3);
    sb.append(" ").append(func);
    Optional.ofNullable(alias).map(Lambdas::getName).ifPresent(as -> sb.append(" as ").append(as));
  }

  @Override
  public <T0, T1, T2, T3, T4> void fun(
      Supplier<String> fun,
      SFunction<T0, ?> param0,
      SFunction<T1, ?> param1,
      SFunction<T2, ?> param2,
      SFunction<T3, ?> param3,
      SFunction<T4, ?> param4) {
    funAs(fun, param0, param1, param2, param3, param4, null);
  }

  @Override
  public <T0, T1, T2, T3, T4, A> void funAs(
      Supplier<String> fun,
      SFunction<T0, ?> param0,
      SFunction<T1, ?> param1,
      SFunction<T2, ?> param2,
      SFunction<T3, ?> param3,
      SFunction<T4, ?> param4,
      SFunction<A, ?> alias) {
    String func = fun.get();
    String para0 = Lambdas.getName(param0);
    String para1 = Lambdas.getName(param1);
    String para2 = Lambdas.getName(param2);
    String para3 = Lambdas.getName(param3);
    String para4 = Lambdas.getName(param4);
    func = func.replace("%0", para0);
    func = func.replace("%1", para1);
    func = func.replace("%2", para2);
    func = func.replace("%3", para3);
    func = func.replace("%4", para4);
    sb.append(" ").append(func);
    Optional.ofNullable(alias).map(Lambdas::getName).ifPresent(as -> sb.append(" as ").append(as));
  }

  @Override
  public void fun(Supplier<String> fun, Supplier<List<? extends SFunction<?, ?>>> paramSupplier) {
    funAs(fun, paramSupplier, null);
  }

  @Override
  public <A> void funAs(
      Supplier<String> fun,
      Supplier<List<? extends SFunction<?, ?>>> paramSupplier,
      SFunction<A, ?> alias) {
    String func = fun.get();
    List<? extends SFunction<?, ?>> sFunctions = paramSupplier.get();
    for (int index = 0; index < sFunctions.size(); index++) {
      func = func.replace("%" + index, Lambdas.getName(sFunctions.get(index)));
    }
    sb.append(" ").append(func);
    Optional.ofNullable(alias).map(Lambdas::getName).ifPresent(as -> sb.append(" as ").append(as));
  }

  @Override
  public StringBuilder selectSql() {
    return sb;
  }
}
