/*
 * Copyright (c) 2018-2022 Tabuyos All Right Reserved.
 */
package com.tabuyos.distributedlocks.zookeeper.lock;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.Watcher;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * ZookeeperLock
 *
 * @author tabuyos
 * @since 2022/5/17
 */
@SuppressWarnings({"ResultOfMethodCallIgnored", "RedundantThrows"})
public class ZookeeperLock implements Lock {

  private static final String ZK_PATH = "/tabuyos/zk-lock";
  private static final String LOCK_PREFIX = ZK_PATH + "/zl-";
  private static final Long WAIT_TIME = 1000L;

  private final CuratorFramework client;

  private String lockedShortPath = null;
  private String lockedPath = null;
  private String priorPath = null;
  private final AtomicInteger lockCount = new AtomicInteger(0);
  private Thread thread;

  public ZookeeperLock() {
    client =
      CuratorFrameworkFactory
        .builder()
        .connectString("127.0.0.1:2181")
        .retryPolicy(new ExponentialBackoffRetry(1000, 3))
        .build();
    client.start();
  }

  @Override
  public boolean lock() throws Exception {
    // 可重入，确保同一线程，可以重复加锁
    synchronized (this) {
      System.out.println("当前lock count: " + lockCount.get() + " 当前线程名: " + Thread.currentThread().getName());
      if (lockCount.get() == 0) {
        thread = Thread.currentThread();
        lockCount.incrementAndGet();
      } else {
        if (!thread.equals(Thread.currentThread())) {
          return false;
        }
        lockCount.incrementAndGet();
        return true;
      }
    }
    try {
      boolean locked;
      // 尝试加锁
      locked = tryLock();
      if (locked) {
        return true;
      }
      // 加锁失败, 等待
      while (!locked) {
        await();
        // 获取等待的子节点列表
        List<String> waiters = getWaiters();
        if (checkLocked(waiters)) {
          locked = true;
        }
      }
      return true;
    } catch (Exception e) {
      e.printStackTrace();
      unlock();
    }
    return false;
  }

  private boolean checkLocked(List<String> waiters) {
    // 节点按照编号，升序排列
    Collections.sort(waiters);
    // 如果是第一个，代表自己已经获得了锁
    if (lockedShortPath.equals(waiters.get(0))) {
      System.out.println("成功的获取分布式锁,节点为: " + lockedShortPath);
      return true;
    }
    return false;
  }

  private List<String> getWaiters() throws Exception {
    return client.getChildren().forPath(ZK_PATH);
  }

  private void await() throws Exception {
    if (null == priorPath) {
      throw new Exception("prior path error");
    }
    final CountDownLatch latch = new CountDownLatch(1);
    Watcher watcher = event -> {
      System.out.println("listen changed! watched event = " + event);
      System.out.println("remove znode");
      latch.countDown();
    };
    client.getData().usingWatcher(watcher).forPath(priorPath);
    latch.await(WAIT_TIME, TimeUnit.SECONDS);
  }

  private boolean tryLock() throws Exception {
    // 创建临时znode
    lockedPath = client.create()
                       .creatingParentsIfNeeded()
                       .withMode(CreateMode.EPHEMERAL_SEQUENTIAL)
                       .forPath(LOCK_PREFIX);
    System.out.println(" 当前线程名: " + Thread.currentThread().getName() + ", 创建节点: " + lockedPath);
    // 获取所有节点
    List<String> waiters = Optional.ofNullable(getWaiters()).orElse(Collections.emptyList());
    if (null == lockedPath) {
      throw new Exception("zk error");
    }
    // 获取加锁的排队编号
    lockedShortPath = getShortPath(lockedPath);
    // 获取等待的子节点列表, 判断自己是否第一个
    if (checkLocked(waiters)) {
      return true;
    }
    // 判断自己排第几个
    int index = Collections.binarySearch(waiters, lockedShortPath);
    // 网络波动导致断线了, 因此列表中已经没有自己了
    if (index < 0) {
      throw new Exception("can't find node: " + lockedShortPath);
    }
    priorPath = ZK_PATH + "/" + waiters.get(index - 1);
    return false;
  }

  private String getShortPath(String lockedPath) {
    int index = lockedPath.lastIndexOf(ZK_PATH + "/");
    if (index >= 0) {
      index += ZK_PATH.length() + 1;
      return index <= lockedPath.length() ? lockedPath.substring(index) : "";
    }
    return null;
  }

  @Override
  public boolean unlock() throws Exception {
    System.out.println("开始解锁");
    System.out.println(lockedPath);
    // 只有加锁的能够解锁(解铃还须系铃人)
    if (!thread.equals(Thread.currentThread())) {
      return false;
    }
    // 减少可重入的计数
    int newLockCount = lockCount.decrementAndGet();
    // 计数不能小于0
    if (newLockCount < 0) {
      throw new IllegalMonitorStateException("lock count has gone negative for lock: " + lockedPath);
    }
    // 如果计数不为0, 直接返回
    if (newLockCount != 0) {
      return true;
    }
    try {
      if (null != client.checkExists().forPath(lockedPath)) {
        client.delete().forPath(lockedPath);
        System.out.println(" 当前线程名: " + Thread.currentThread().getName() + ", 删除节点: " + lockedPath);
      }
    } catch (Exception e) {
      e.printStackTrace();
      return false;
    }
    return true;
  }
}
