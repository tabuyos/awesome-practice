/*
 * Copyright (c) 2018-2021 Tabuyos All Right Reserved.
 */
package com.tabuyos.zookeeper.constant;

/**
 * ZookeeperServiceConstant
 *
 * @author tabuyos
 * @since 2021/12/30
 */
public class ZookeeperServiceConstant {

  /** zookeeper session timeout */
  public static final int ZK_SESSION_TIMEOUT = 5_000;

  /** zookeeper connection timeout */
  public static final int ZK_CONNECTION_TIMEOUT = 5_000;

  /** zookeeper registry path */
  public static final String ZK_REGISTRY_PATH = "/registry";

  /** zookeeper children path */
  public static final String ZK_CHILDREN_PATH = ZK_REGISTRY_PATH + "/data";
}
