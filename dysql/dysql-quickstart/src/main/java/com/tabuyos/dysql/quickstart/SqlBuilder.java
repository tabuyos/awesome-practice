/*
 * copyright(c) 2018-2023 tabuyos all right reserved.
 */
package com.tabuyos.dysql.quickstart;

import java.util.function.Consumer;

/**
 * SqlBuilder
 *
 * @author tabuyos
 * @since 2023/5/31
 */
public interface SqlBuilder {
  SqlBuilder select(Consumer<SelectContext> ctxCme);

  SqlBuilder from(Consumer<FromContext> ctxCme);
  SqlBuilder where(Consumer<WhereContext> ctxCme);
}
