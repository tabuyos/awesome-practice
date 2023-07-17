/*
 * copyright(c) 2018-2023 tabuyos all right reserved.
 */
package com.tabuyos.dysql.quickstart;

import java.io.Serializable;
import java.util.function.Function;

/**
 * SFunction
 *
 * @author tabuyos
 * @since 2023/7/17
 */
@FunctionalInterface
public interface SFunction<T, R> extends Function<T, R>, Serializable {}
