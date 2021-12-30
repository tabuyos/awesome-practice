/*
 * Copyright (c) 2018-2021 Tabuyos All Right Reserved.
 */
package com.tabuyos.zookeeper.service.impl;

import com.tabuyos.zookeeper.constant.ZookeeperServiceConstant;
import com.tabuyos.zookeeper.service.RegistryService;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.imps.CuratorFrameworkState;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

/**
 * RegistryServiceImpl
 *
 * @author tabuyos
 * @since 2021/12/30
 */
@SuppressWarnings("unused")
public class RegistryServiceImpl implements RegistryService {

  private final CuratorFramework curatorFramework;
  private final Logger logger = LoggerFactory.getLogger(RegistryServiceImpl.class);
  private final CountDownLatch countDownLatch = new CountDownLatch(1);

  public RegistryServiceImpl(String address) {
    this.curatorFramework =
        CuratorFrameworkFactory.builder()
            .connectString(address)
            .connectionTimeoutMs(ZookeeperServiceConstant.ZK_CONNECTION_TIMEOUT)
            .sessionTimeoutMs(ZookeeperServiceConstant.ZK_SESSION_TIMEOUT)
            .retryPolicy(new ExponentialBackoffRetry(1000, 3))
            .build();
  }

  @Override
  public void registry(String key, String value) throws Exception {
    CuratorFrameworkState state = curatorFramework.getState();
    if (!state.equals(CuratorFrameworkState.STARTED)) {
      logger.info("zookeeper 客户端启动");
      curatorFramework.start();
    }
    String data = key + "=" + value;
    String path = ZookeeperServiceConstant.ZK_CHILDREN_PATH;
    curatorFramework
        .create()
        .creatingParentsIfNeeded()
        .withMode(CreateMode.EPHEMERAL_SEQUENTIAL)
        .forPath(path, data.getBytes());
  }

  @Override
  public void close() throws IOException {
    if (curatorFramework == null) {
      return;
    }
    CuratorFrameworkState state = curatorFramework.getState();
    if (state == CuratorFrameworkState.STOPPED) {
      return;
    }
    curatorFramework.close();
  }
}
