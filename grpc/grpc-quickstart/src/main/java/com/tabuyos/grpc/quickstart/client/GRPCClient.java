/*
 * Copyright (c) 2018-2022 Tabuyos All Right Reserved.
 */
package com.tabuyos.grpc.quickstart.client;

import com.tabuyos.grpc.quickstart.api.RPCDateServiceApi;
import com.tabuyos.grpc.quickstart.api.RPCDateServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

/**
 * GRPCClient
 *
 * @author tabuyos
 * @since 2022/1/20
 */
public class GRPCClient {

  private static final String host = "localhost";
  private static final int serverPort = 9999;

  public static void main(String[] args) throws Exception {
    // 1. 拿到一个通信的channel
    ManagedChannel managedChannel = ManagedChannelBuilder.forAddress(host, serverPort).usePlaintext().build();
    try {
      // 2.拿到道理对象
      RPCDateServiceGrpc.RPCDateServiceBlockingStub rpcDateService = RPCDateServiceGrpc.newBlockingStub(managedChannel);
      RPCDateServiceApi.RPCDateRequest rpcDateRequest = RPCDateServiceApi.RPCDateRequest
        .newBuilder()
        .setUserName("tabuyos")
        .build();
      // 3. 请求
      RPCDateServiceApi.RPCDateResponse rpcDateResponse = rpcDateService.getDate(rpcDateRequest);
      // 4. 输出结果
      System.out.println(rpcDateResponse.getServerDate());
    } finally {
      // 5.关闭channel, 释放资源.
      managedChannel.shutdown();
    }
  }
}
