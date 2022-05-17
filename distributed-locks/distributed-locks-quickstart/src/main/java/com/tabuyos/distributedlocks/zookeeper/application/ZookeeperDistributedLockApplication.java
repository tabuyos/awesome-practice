/*
 * Copyright (c) 2018-2022 Tabuyos All Right Reserved.
 */
package com.tabuyos.distributedlocks.zookeeper.application;

import com.tabuyos.distributedlocks.zookeeper.lock.Lock;
import com.tabuyos.distributedlocks.zookeeper.lock.ZookeeperLock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * ZookeeperDistributedLockApplication
 *
 * @author tabuyos
 * @since 2022/5/17
 */
public class ZookeeperDistributedLockApplication {

  public static void main(String[] args) throws InterruptedException {
    ScheduledThreadPoolExecutor scheduledThreadPoolExecutor = new ScheduledThreadPoolExecutor(10);
    for (int i = 0; i < 10; i++) {

      scheduledThreadPoolExecutor.submit(() -> {
        int count = 0;
        //创建锁
        Lock lock = new ZookeeperLock();
        try {
          lock.lock();
        } catch (Exception e) {
          e.printStackTrace();
        }
        //每条线程，执行10次累加
        for (int j = 0; j < 10; j++) {
          //公共的资源变量累加
          count++;
        }
        try {
          System.out.println("开始睡眠!");
          Thread.sleep(1000);
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
        System.out.println("count = " + count);
        //释放锁
        try {
          lock.unlock();
        } catch (Exception e) {
          e.printStackTrace();
        }

      });
    }

    Thread.sleep(Integer.MAX_VALUE);
  }
}
