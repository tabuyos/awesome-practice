/*
 * Copyright (c) 2018-2021 Tabuyos All Right Reserved.
 */
package com.tabuyos.zookeeper.service.impl;

import com.tabuyos.zookeeper.constant.ZookeeperServiceConstant;
import com.tabuyos.zookeeper.service.RemoveService;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.imps.CuratorFrameworkState;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * RemoveServiceImpl
 *
 * @author tabuyos
 * @since 2021/12/30
 */
@SuppressWarnings({"unused", "FieldMayBeFinal"})
public class RemoveServiceImpl implements RemoveService {

  private final CuratorFramework curatorFramework;
  private final Logger logger = LoggerFactory.getLogger(RegistryServiceImpl.class);
  private volatile Map<String, String> concurrentHashMap = new ConcurrentHashMap<>(100);

  public RemoveServiceImpl(CuratorFramework curatorFramework) {
    this.curatorFramework = curatorFramework;
  }

  public RemoveServiceImpl(String address) throws Exception {
    this.curatorFramework =
        CuratorFrameworkFactory.builder()
            .connectString(address)
            .connectionTimeoutMs(ZookeeperServiceConstant.ZK_CONNECTION_TIMEOUT)
            .sessionTimeoutMs(ZookeeperServiceConstant.ZK_SESSION_TIMEOUT)
            .retryPolicy(new ExponentialBackoffRetry(1000, 3))
            .build();
    watchChildNode(curatorFramework);
  }

  private void watchChildNode(final CuratorFramework client) throws Exception {
    if (!client.getState().equals(CuratorFrameworkState.STARTED)) {
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
      String path = ZookeeperServiceConstant.ZK_REGISTRY_PATH + "/" + node;
      byte[] data = client.getData().forPath(path);
      concurrentHashMap.put(new String(data), path);
    }
  }

  @Override
  public void remove(String key) throws Exception {
    if (!curatorFramework.getState().equals(CuratorFrameworkState.STARTED)) {
      logger.info("zookeeper 客户端启动");
      curatorFramework.start();
    }
    Stat stat = new Stat();
    for (String mapKey : concurrentHashMap.keySet()) {
      logger.info(mapKey);
      String mapValue = concurrentHashMap.get(mapKey);
      String[] split = mapKey.split("=");
      if (split.length == 2 && key.equals(split[0])) {
        logger.info("移除 {} 节点, path: {}", split[1], mapValue);
        curatorFramework.getData().storingStatIn(stat).forPath(mapValue);
        curatorFramework
            .delete()
            .deletingChildrenIfNeeded()
            .withVersion(stat.getVersion())
            .forPath(mapValue);
      }
    }
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
