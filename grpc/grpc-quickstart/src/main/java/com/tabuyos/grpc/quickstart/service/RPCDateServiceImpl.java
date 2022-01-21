/*
 * Copyright (c) 2018-2022 Tabuyos All Right Reserved.
 */
package com.tabuyos.grpc.quickstart.service;

import com.tabuyos.grpc.quickstart.api.RPCDateServiceApi;
import com.tabuyos.grpc.quickstart.api.RPCDateServiceGrpc;
import io.grpc.stub.StreamObserver;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * RPCDateServiceImplBase
 *
 * @author tabuyos
 * @since 2022/1/20
 */
public class RPCDateServiceImpl extends RPCDateServiceGrpc.RPCDateServiceImplBase {

  @Override
  public void getDate(RPCDateServiceApi.RPCDateRequest request,
                      StreamObserver<RPCDateServiceApi.RPCDateResponse> responseObserver) {
    // 请求结果,我们定义的
    RPCDateServiceApi.RPCDateResponse rpcDateResponse = null;
    String userName = request.getUserName();
    String response = String.format("你好: %s, 今天是%s.",
                                    userName,
                                    LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
    try {
      // 定义响应,是一个builder构造器.
      rpcDateResponse = RPCDateServiceApi.RPCDateResponse
        .newBuilder()
        .setServerDate(response)
        .build();
    } catch (Exception e) {
      responseObserver.onError(e);
    } finally {
      // 这种写法是observer,异步写法,老外喜欢用这个框架.
      responseObserver.onNext(rpcDateResponse);
    }
    responseObserver.onCompleted();
  }
}
