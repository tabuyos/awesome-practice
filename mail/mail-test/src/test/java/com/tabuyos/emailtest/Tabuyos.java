/*
 * copyright(c) 2018-2021 tabuyos all right reserved.
 */
package com.tabuyos.emailtest;

import java.util.function.Function;

/**
 * Tabuyos
 *
 * @author tabuyos
 * @since 2021/11/23
 */
public class Tabuyos {

  public static void main(String[] args) {
    ConcurrentOperation function =
        (a, b) -> {
          System.out.println(a);
          System.out.println(b);
          return a + b;
        };
    RequireOperation operation = (a) -> (b) -> a.execute(b, 1);
    System.out.println(function.execute(1, 2));
    System.out.println(operation.operation(function).apply(5));
  }

  @FunctionalInterface
  public interface ConcurrentOperation {
    int execute(int threadNumber, int step);
  }

  @FunctionalInterface
  public interface RequireOperation {
    Function<Integer, Integer> operation(ConcurrentOperation operation);
  }
}
