/*
 * copyright(c) 2018-2023 tabuyos all right reserved.
 */
package com.tabuyos.dysql.quickstart;

import java.io.Serializable;
import java.util.function.Supplier;

/**
 * SSupplier
 *
 * @author tabuyos
 * @since 2023/7/17
 */
@FunctionalInterface
public interface SSupplier<R> extends Supplier<R>, Serializable {}
