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
public interface FromContext {
  void table(String name);

  void join(String name, String condition);

  void leftJoin(String name, String condition);

  void rightJoin(String name, String condition);
}
