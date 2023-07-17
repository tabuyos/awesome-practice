/*
 * copyright(c) 2018-2023 tabuyos all right reserved.
 */
package com.tabuyos.dysql.quickstart;

import java.util.function.Supplier;

/**
 * WhereContext
 *
 * @author tabuyos
 * @since 2023/6/20
 */
public interface WhereContext {
  void cond(String condition);

  void cond(String condition, Object... parameters);

  <T, V> void cond(SFunction<T, V> col, String op, V... parameters);

  void fun(String fun);

  void fun(Supplier<String> fun, Object... parameters);

  void and(String condition);

  void and(Runnable runnable);

  void or(String condition);

  void or(Runnable runnable);
}
