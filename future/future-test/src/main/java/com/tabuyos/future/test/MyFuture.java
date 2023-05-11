/*
 * copyright(c) 2018-2022 tabuyos all right reserved.
 */
package com.tabuyos.future.test;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * MyFuture
 *
 * @author tabuyos
 * @since 2022/3/8
 */
public class MyFuture {

  public static void main(String[] args) throws ExecutionException, InterruptedException {
    Future<String> future = CompletableFuture.completedFuture("im future.");
    System.out.println(future.get());
  }
}
