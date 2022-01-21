package com.tabuyos.grpc.quickstart.api;

import static io.grpc.MethodDescriptor.generateFullMethodName;

/**
 * <pre>
 * 服务接口.定义请求参数和相应结果
 * </pre>
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.43.2)",
    comments = "Source: RPCDateService.proto")
@io.grpc.stub.annotations.GrpcGenerated
public final class RPCDateServiceGrpc {

  private RPCDateServiceGrpc() {}

  public static final String SERVICE_NAME = "com.tabuyos.grpc.quickstart.api.RPCDateService";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<com.tabuyos.grpc.quickstart.api.RPCDateServiceApi.RPCDateRequest,
      com.tabuyos.grpc.quickstart.api.RPCDateServiceApi.RPCDateResponse> getGetDateMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "getDate",
      requestType = com.tabuyos.grpc.quickstart.api.RPCDateServiceApi.RPCDateRequest.class,
      responseType = com.tabuyos.grpc.quickstart.api.RPCDateServiceApi.RPCDateResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.tabuyos.grpc.quickstart.api.RPCDateServiceApi.RPCDateRequest,
      com.tabuyos.grpc.quickstart.api.RPCDateServiceApi.RPCDateResponse> getGetDateMethod() {
    io.grpc.MethodDescriptor<com.tabuyos.grpc.quickstart.api.RPCDateServiceApi.RPCDateRequest, com.tabuyos.grpc.quickstart.api.RPCDateServiceApi.RPCDateResponse> getGetDateMethod;
    if ((getGetDateMethod = RPCDateServiceGrpc.getGetDateMethod) == null) {
      synchronized (RPCDateServiceGrpc.class) {
        if ((getGetDateMethod = RPCDateServiceGrpc.getGetDateMethod) == null) {
          RPCDateServiceGrpc.getGetDateMethod = getGetDateMethod =
              io.grpc.MethodDescriptor.<com.tabuyos.grpc.quickstart.api.RPCDateServiceApi.RPCDateRequest, com.tabuyos.grpc.quickstart.api.RPCDateServiceApi.RPCDateResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "getDate"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.tabuyos.grpc.quickstart.api.RPCDateServiceApi.RPCDateRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.tabuyos.grpc.quickstart.api.RPCDateServiceApi.RPCDateResponse.getDefaultInstance()))
              .setSchemaDescriptor(new RPCDateServiceMethodDescriptorSupplier("getDate"))
              .build();
        }
      }
    }
    return getGetDateMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static RPCDateServiceStub newStub(io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<RPCDateServiceStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<RPCDateServiceStub>() {
        @java.lang.Override
        public RPCDateServiceStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new RPCDateServiceStub(channel, callOptions);
        }
      };
    return RPCDateServiceStub.newStub(factory, channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static RPCDateServiceBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<RPCDateServiceBlockingStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<RPCDateServiceBlockingStub>() {
        @java.lang.Override
        public RPCDateServiceBlockingStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new RPCDateServiceBlockingStub(channel, callOptions);
        }
      };
    return RPCDateServiceBlockingStub.newStub(factory, channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static RPCDateServiceFutureStub newFutureStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<RPCDateServiceFutureStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<RPCDateServiceFutureStub>() {
        @java.lang.Override
        public RPCDateServiceFutureStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new RPCDateServiceFutureStub(channel, callOptions);
        }
      };
    return RPCDateServiceFutureStub.newStub(factory, channel);
  }

  /**
   * <pre>
   * 服务接口.定义请求参数和相应结果
   * </pre>
   */
  public static abstract class RPCDateServiceImplBase implements io.grpc.BindableService {

    /**
     */
    public void getDate(com.tabuyos.grpc.quickstart.api.RPCDateServiceApi.RPCDateRequest request,
        io.grpc.stub.StreamObserver<com.tabuyos.grpc.quickstart.api.RPCDateServiceApi.RPCDateResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getGetDateMethod(), responseObserver);
    }

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
          .addMethod(
            getGetDateMethod(),
            io.grpc.stub.ServerCalls.asyncUnaryCall(
              new MethodHandlers<
                com.tabuyos.grpc.quickstart.api.RPCDateServiceApi.RPCDateRequest,
                com.tabuyos.grpc.quickstart.api.RPCDateServiceApi.RPCDateResponse>(
                  this, METHODID_GET_DATE)))
          .build();
    }
  }

  /**
   * <pre>
   * 服务接口.定义请求参数和相应结果
   * </pre>
   */
  public static final class RPCDateServiceStub extends io.grpc.stub.AbstractAsyncStub<RPCDateServiceStub> {
    private RPCDateServiceStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected RPCDateServiceStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new RPCDateServiceStub(channel, callOptions);
    }

    /**
     */
    public void getDate(com.tabuyos.grpc.quickstart.api.RPCDateServiceApi.RPCDateRequest request,
        io.grpc.stub.StreamObserver<com.tabuyos.grpc.quickstart.api.RPCDateServiceApi.RPCDateResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getGetDateMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   * <pre>
   * 服务接口.定义请求参数和相应结果
   * </pre>
   */
  public static final class RPCDateServiceBlockingStub extends io.grpc.stub.AbstractBlockingStub<RPCDateServiceBlockingStub> {
    private RPCDateServiceBlockingStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected RPCDateServiceBlockingStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new RPCDateServiceBlockingStub(channel, callOptions);
    }

    /**
     */
    public com.tabuyos.grpc.quickstart.api.RPCDateServiceApi.RPCDateResponse getDate(com.tabuyos.grpc.quickstart.api.RPCDateServiceApi.RPCDateRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getGetDateMethod(), getCallOptions(), request);
    }
  }

  /**
   * <pre>
   * 服务接口.定义请求参数和相应结果
   * </pre>
   */
  public static final class RPCDateServiceFutureStub extends io.grpc.stub.AbstractFutureStub<RPCDateServiceFutureStub> {
    private RPCDateServiceFutureStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected RPCDateServiceFutureStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new RPCDateServiceFutureStub(channel, callOptions);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.tabuyos.grpc.quickstart.api.RPCDateServiceApi.RPCDateResponse> getDate(
        com.tabuyos.grpc.quickstart.api.RPCDateServiceApi.RPCDateRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getGetDateMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_GET_DATE = 0;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final RPCDateServiceImplBase serviceImpl;
    private final int methodId;

    MethodHandlers(RPCDateServiceImplBase serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_GET_DATE:
          serviceImpl.getDate((com.tabuyos.grpc.quickstart.api.RPCDateServiceApi.RPCDateRequest) request,
              (io.grpc.stub.StreamObserver<com.tabuyos.grpc.quickstart.api.RPCDateServiceApi.RPCDateResponse>) responseObserver);
          break;
        default:
          throw new AssertionError();
      }
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public io.grpc.stub.StreamObserver<Req> invoke(
        io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        default:
          throw new AssertionError();
      }
    }
  }

  private static abstract class RPCDateServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    RPCDateServiceBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return com.tabuyos.grpc.quickstart.api.RPCDateServiceApi.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("RPCDateService");
    }
  }

  private static final class RPCDateServiceFileDescriptorSupplier
      extends RPCDateServiceBaseDescriptorSupplier {
    RPCDateServiceFileDescriptorSupplier() {}
  }

  private static final class RPCDateServiceMethodDescriptorSupplier
      extends RPCDateServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final String methodName;

    RPCDateServiceMethodDescriptorSupplier(String methodName) {
      this.methodName = methodName;
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.MethodDescriptor getMethodDescriptor() {
      return getServiceDescriptor().findMethodByName(methodName);
    }
  }

  private static volatile io.grpc.ServiceDescriptor serviceDescriptor;

  public static io.grpc.ServiceDescriptor getServiceDescriptor() {
    io.grpc.ServiceDescriptor result = serviceDescriptor;
    if (result == null) {
      synchronized (RPCDateServiceGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new RPCDateServiceFileDescriptorSupplier())
              .addMethod(getGetDateMethod())
              .build();
        }
      }
    }
    return result;
  }
}
