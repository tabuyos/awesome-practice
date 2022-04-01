/*
 * Copyright (c) 2018-2022 Tabuyos All Right Reserved.
 */
package com.tabuyos.caffeine.quickstart;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import com.github.benmanes.caffeine.cache.Scheduler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * CaffeineQuickstartApplication
 *
 * @author tabuyos
 * @since 2022/3/30
 */
@SuppressWarnings({"AlibabaAvoidManuallyCreateThread", "AlibabaThreadPoolCreation"})
public class CaffeineQuickstartApplication {

  private static final Logger logger = LoggerFactory.getLogger(CaffeineQuickstartApplication.class);

  public static void main(String[] args) throws InterruptedException {
    cache();
    // loadingCache();
  }

  public static void cache() throws InterruptedException {
    Cache<String, Object> cache = Caffeine.newBuilder()
                                          // 初始的缓存空间大小
                                          .initialCapacity(100)
                                          // 缓存的最大条数
                                          .maximumSize(1000)
                                          // 设置最后一次写入或访问后经过固定时间过期
                                          .expireAfterWrite(10, TimeUnit.SECONDS)
                                          // 配置调度
                                          .scheduler(Scheduler.forScheduledExecutorService(Executors.newScheduledThreadPool(
                                            1)))
                                          // 监听器
                                          .removalListener(
                                            (key, graph, cause) ->
                                            {
                                              System.out.println("--------------------------");
                                              // System.out.println(key);
                                              // System.out.println(graph);
                                              // System.out.println(cause);
                                              // logger.info("移除缓存中key为：{},value为：{}的缓存记录", key, graph);
                                            })
                                          // 使用软引用存储值
                                          .softValues()
                                          .build();

    cache.put("username", "tabuyos");
    Map<String, Object> usernameMap = cache.getAllPresent(Collections.singletonList("username"));
    System.out.println(usernameMap);
    Object username = cache.get("usernames", key -> {
      System.out.println(key);
      return "aaronliew";
    });
    System.out.println(username);
    System.out.println(cache.asMap());
    System.out.println("================================");
    while (true) {
      System.out.println("sleeping...");
      // 如果没有配置scheduler调度线程, 那么只有当调用cache时才会去触发监听器
      cache.put("username", cache.getIfPresent("username"));
      System.out.println(cache.asMap());
      // System.out.println(cache.getIfPresent("username"));
      TimeUnit.SECONDS.sleep(4);
    }
  }

  public static void loadingCache() throws InterruptedException {
    LoadingCache<String, Object> cache = Caffeine.newBuilder()
                                                 // 初始的缓存空间大小
                                                 .initialCapacity(100)
                                                 // 缓存的最大条数
                                                 .maximumSize(1000)
                                                 // 设置最后一次写入或访问后经过固定时间过期
                                                 .expireAfterWrite(10, TimeUnit.SECONDS)
                                                 .refreshAfterWrite(3, TimeUnit.SECONDS)
                                                 // 配置调度
                                                 .scheduler(Scheduler.forScheduledExecutorService(Executors.newScheduledThreadPool(
                                                   1)))
                                                 // 监听器
                                                 .removalListener(
                                                   (key, graph, cause) ->
                                                   {
                                                     System.out.println("--------------------------");
                                                     // System.out.println(key);
                                                     // System.out.println(graph);
                                                     // System.out.println(cause);
                                                     // logger.info("移除缓存中key为：{},value为：{}的缓存记录", key, graph);
                                                   })
                                                 // 使用软引用存储值
                                                 .softValues()
                                                 .build(key -> key);

    cache.put("username", "tabuyos");
    Map<String, Object> usernameMap = cache.getAllPresent(Collections.singletonList("username"));
    System.out.println(usernameMap);
    Object username = cache.get("usernames", key -> {
      System.out.println(key);
      return "aaronliew";
    });
    System.out.println(username);
    System.out.println(cache.asMap());
    System.out.println("================================");
    TimeUnit.SECONDS.sleep(4);
    System.out.println(cache.getIfPresent("username"));
    while (true) {
      System.out.println("sleeping...");
      // 如果没有配置scheduler调度线程, 那么只有当调用cache时才会去触发监听器
      System.out.println(cache.asMap());
      // System.out.println(cache.getIfPresent("username"));
      TimeUnit.SECONDS.sleep(4);
    }
  }
}
