/*
 * copyright(c) 2018-2023 tabuyos all right reserved.
 */
package com.tabuyos.dysql.quickstart;
/**
 * WhereContext
 *
 * @author tabuyos
 * @since 2023/6/20
 */
public interface WhereContext {
  void cond(String condition);

  void and(String condition);

  void and(Runnable runnable);

  void or(String condition);

  void or(Runnable runnable);

  void cond(String condition, Object... parameters);
}
