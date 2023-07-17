/*
 * copyright(c) 2018-2023 tabuyos all right reserved.
 */
package com.tabuyos.dysql.quickstart;

import java.util.function.Consumer;

/**
 * QuerySqlBuilder
 *
 * @author tabuyos
 * @since 2023/7/17
 */
public class QuerySqlBuilder implements SqlBuilder {
  @Override
  public SqlBuilder select(Consumer<SelectContext> ctxCme) {
    return null;
  }

  @Override
  public SqlBuilder from(Consumer<FromContext> ctxCme) {
    return null;
  }

  @Override
  public SqlBuilder where(Consumer<WhereContext> ctxCme) {
    return null;
  }

  @Override
  public String sql() {
    return null;
  }
}
