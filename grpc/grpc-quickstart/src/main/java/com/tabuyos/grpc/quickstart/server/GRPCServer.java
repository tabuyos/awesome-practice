/*
 * Copyright (c) 2018-2022 Tabuyos All Right Reserved.
 */
package com.tabuyos.grpc.quickstart.server;

import com.tabuyos.grpc.quickstart.service.RPCDateServiceImpl;
import io.grpc.Server;
import io.grpc.ServerBuilder;

import java.util.concurrent.TimeUnit;

/**
 * GRPCServer
 *
 * @author tabuyos
 * @since 2022/1/20
 */
public class GRPCServer {

  private static final int PORT = 9999;

  public static void main(String[] args) throws Exception {
    // 设置service接口.
    Server server = ServerBuilder.
      forPort(PORT)
      .addService(new RPCDateServiceImpl())
      .build()
      .start();
    Runtime.getRuntime().addShutdownHook(new Thread(() -> {
      System.out.println("*** shutting down gRPC server since JVM is shutting down");
      try {
        server.shutdown().awaitTermination(30, TimeUnit.SECONDS);
      } catch (InterruptedException e) {
        e.printStackTrace(System.err);
      }
      System.out.println("*** server shut down");
    }));
    System.out.printf("GRpc服务端启动成功, 端口号: %d.%n", PORT);
    server.awaitTermination();
  }
}
