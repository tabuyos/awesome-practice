/*
 * Copyright (c) 2018-2021 Tabuyos All Right Reserved.
 */
package com.tabuyos.zookeeper.service.impl;

import com.tabuyos.zookeeper.constant.ZookeeperServiceConstant;
import com.tabuyos.zookeeper.service.DiscoverService;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.imps.CuratorFrameworkState;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.Watcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;

/**
 * DiscoverServiceImpl
 *
 * @author tabuyos
 * @since 2021/12/30
 */
@SuppressWarnings({"unused", "FieldMayBeFinal"})
public class DiscoverServiceImpl implements DiscoverService {

  private final CuratorFramework curatorFramework;
  private static final Logger logger = LoggerFactory.getLogger(DiscoverServiceImpl.class);
  private volatile List<String> dataList = new ArrayList<>();

  public DiscoverServiceImpl(String address) throws Exception {
    this.curatorFramework =
        CuratorFrameworkFactory.builder()
            .connectString(address)
            .connectionTimeoutMs(ZookeeperServiceConstant.ZK_CONNECTION_TIMEOUT)
            .sessionTimeoutMs(ZookeeperServiceConstant.ZK_SESSION_TIMEOUT)
            .retryPolicy(new ExponentialBackoffRetry(1000, 3))
            .build();
    watchChildNode(curatorFramework);
  }

  public DiscoverServiceImpl(CuratorFramework curatorFramework) {
    this.curatorFramework = curatorFramework;
  }

  private void watchChildNode(final CuratorFramework client) throws Exception {
    CuratorFrameworkState state = client.getState();
    if (!state.equals(CuratorFrameworkState.STARTED)) {
      logger.info("zookeeper 客户端启动");
      client.start();
    }
    List<String> nodeList =
        client
            .getChildren()
            .usingWatcher(
                (Watcher)
                    watchedEvent -> {
                      if (watchedEvent.getType() == Watcher.Event.EventType.NodeChildrenChanged) {
                        try {
                          watchChildNode(client);
                        } catch (Exception e) {
                          e.printStackTrace();
                        }
                      }
                    })
            .forPath(ZookeeperServiceConstant.ZK_REGISTRY_PATH);
    nodeList.forEach(logger::info);
    for (String node : nodeList) {
      byte[] data =
          client.getData().forPath(ZookeeperServiceConstant.ZK_REGISTRY_PATH + "/" + node);
      dataList.add(new String(data));
    }
  }

  @Override
  public String discover(String key) {
    dataList.forEach(logger::info);
    String data = null;
    List<String> addresses = getAddresses(dataList, key);
    int size = addresses.size();
    logger.info("发现与 {} 相关的节点: {}", key, addresses);
    if (size > 0) {
      if (size == 1) {
        data = addresses.get(0);
      } else {
        data = addresses.get(ThreadLocalRandom.current().nextInt(size));
      }
    }
    return data;
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

  private List<String> getAddresses(List<String> list, String prefix) {
    List<String> addresses = new ArrayList<>();
    final int length = 2;
    list.stream()
        .filter(Objects::nonNull)
        .filter(el -> el.startsWith(prefix))
        .forEach(
            el -> {
              String[] split = el.split("=");
              if (split.length == length && prefix.equals(split[0])) {
                addresses.add(split[1]);
              }
            });
    return addresses;
  }
}
