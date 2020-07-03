package com.example.token;

import static io.grpc.stub.ClientCalls.asyncUnaryCall;
import static io.grpc.stub.ClientCalls.asyncServerStreamingCall;
import static io.grpc.stub.ClientCalls.asyncClientStreamingCall;
import static io.grpc.stub.ClientCalls.asyncBidiStreamingCall;
import static io.grpc.stub.ClientCalls.blockingUnaryCall;
import static io.grpc.stub.ClientCalls.blockingServerStreamingCall;
import static io.grpc.stub.ClientCalls.futureUnaryCall;
import static io.grpc.MethodDescriptor.generateFullMethodName;
import static io.grpc.stub.ServerCalls.asyncUnaryCall;
import static io.grpc.stub.ServerCalls.asyncServerStreamingCall;
import static io.grpc.stub.ServerCalls.asyncClientStreamingCall;
import static io.grpc.stub.ServerCalls.asyncBidiStreamingCall;
import static io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall;
import static io.grpc.stub.ServerCalls.asyncUnimplementedStreamingCall;

/**
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.7.0)",
    comments = "Source: NodeService.proto")
public final class NodeServiceGrpc {

  private NodeServiceGrpc() {}

  public static final String SERVICE_NAME = "com.example.token.NodeService";

  // Static method descriptors that strictly reflect the proto.
  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  public static final io.grpc.MethodDescriptor<com.example.token.NodeServiceOuterClass.JoinRequest,
      com.example.token.NodeServiceOuterClass.JoinResponse> METHOD_JOIN_NETWORK =
      io.grpc.MethodDescriptor.<com.example.token.NodeServiceOuterClass.JoinRequest, com.example.token.NodeServiceOuterClass.JoinResponse>newBuilder()
          .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
          .setFullMethodName(generateFullMethodName(
              "com.example.token.NodeService", "joinNetwork"))
          .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
              com.example.token.NodeServiceOuterClass.JoinRequest.getDefaultInstance()))
          .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
              com.example.token.NodeServiceOuterClass.JoinResponse.getDefaultInstance()))
          .setSchemaDescriptor(new NodeServiceMethodDescriptorSupplier("joinNetwork"))
          .build();
  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  public static final io.grpc.MethodDescriptor<com.example.token.NodeServiceOuterClass.TokenData,
      com.example.token.NodeServiceOuterClass.Empty> METHOD_TOKEN_DELIVERY_DATA =
      io.grpc.MethodDescriptor.<com.example.token.NodeServiceOuterClass.TokenData, com.example.token.NodeServiceOuterClass.Empty>newBuilder()
          .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
          .setFullMethodName(generateFullMethodName(
              "com.example.token.NodeService", "tokenDeliveryData"))
          .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
              com.example.token.NodeServiceOuterClass.TokenData.getDefaultInstance()))
          .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
              com.example.token.NodeServiceOuterClass.Empty.getDefaultInstance()))
          .setSchemaDescriptor(new NodeServiceMethodDescriptorSupplier("tokenDeliveryData"))
          .build();
  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  public static final io.grpc.MethodDescriptor<com.example.token.NodeServiceOuterClass.TokenDelete,
      com.example.token.NodeServiceOuterClass.Empty> METHOD_TOKEN_DELIVERY_DELETE =
      io.grpc.MethodDescriptor.<com.example.token.NodeServiceOuterClass.TokenDelete, com.example.token.NodeServiceOuterClass.Empty>newBuilder()
          .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
          .setFullMethodName(generateFullMethodName(
              "com.example.token.NodeService", "tokenDeliveryDelete"))
          .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
              com.example.token.NodeServiceOuterClass.TokenDelete.getDefaultInstance()))
          .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
              com.example.token.NodeServiceOuterClass.Empty.getDefaultInstance()))
          .setSchemaDescriptor(new NodeServiceMethodDescriptorSupplier("tokenDeliveryDelete"))
          .build();

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static NodeServiceStub newStub(io.grpc.Channel channel) {
    return new NodeServiceStub(channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static NodeServiceBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    return new NodeServiceBlockingStub(channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static NodeServiceFutureStub newFutureStub(
      io.grpc.Channel channel) {
    return new NodeServiceFutureStub(channel);
  }

  /**
   */
  public static abstract class NodeServiceImplBase implements io.grpc.BindableService {

    /**
     */
    public void joinNetwork(com.example.token.NodeServiceOuterClass.JoinRequest request,
        io.grpc.stub.StreamObserver<com.example.token.NodeServiceOuterClass.JoinResponse> responseObserver) {
      asyncUnimplementedUnaryCall(METHOD_JOIN_NETWORK, responseObserver);
    }

    /**
     */
    public void tokenDeliveryData(com.example.token.NodeServiceOuterClass.TokenData request,
        io.grpc.stub.StreamObserver<com.example.token.NodeServiceOuterClass.Empty> responseObserver) {
      asyncUnimplementedUnaryCall(METHOD_TOKEN_DELIVERY_DATA, responseObserver);
    }

    /**
     */
    public void tokenDeliveryDelete(com.example.token.NodeServiceOuterClass.TokenDelete request,
        io.grpc.stub.StreamObserver<com.example.token.NodeServiceOuterClass.Empty> responseObserver) {
      asyncUnimplementedUnaryCall(METHOD_TOKEN_DELIVERY_DELETE, responseObserver);
    }

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
          .addMethod(
            METHOD_JOIN_NETWORK,
            asyncUnaryCall(
              new MethodHandlers<
                com.example.token.NodeServiceOuterClass.JoinRequest,
                com.example.token.NodeServiceOuterClass.JoinResponse>(
                  this, METHODID_JOIN_NETWORK)))
          .addMethod(
            METHOD_TOKEN_DELIVERY_DATA,
            asyncUnaryCall(
              new MethodHandlers<
                com.example.token.NodeServiceOuterClass.TokenData,
                com.example.token.NodeServiceOuterClass.Empty>(
                  this, METHODID_TOKEN_DELIVERY_DATA)))
          .addMethod(
            METHOD_TOKEN_DELIVERY_DELETE,
            asyncUnaryCall(
              new MethodHandlers<
                com.example.token.NodeServiceOuterClass.TokenDelete,
                com.example.token.NodeServiceOuterClass.Empty>(
                  this, METHODID_TOKEN_DELIVERY_DELETE)))
          .build();
    }
  }

  /**
   */
  public static final class NodeServiceStub extends io.grpc.stub.AbstractStub<NodeServiceStub> {
    private NodeServiceStub(io.grpc.Channel channel) {
      super(channel);
    }

    private NodeServiceStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected NodeServiceStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new NodeServiceStub(channel, callOptions);
    }

    /**
     */
    public void joinNetwork(com.example.token.NodeServiceOuterClass.JoinRequest request,
        io.grpc.stub.StreamObserver<com.example.token.NodeServiceOuterClass.JoinResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(METHOD_JOIN_NETWORK, getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void tokenDeliveryData(com.example.token.NodeServiceOuterClass.TokenData request,
        io.grpc.stub.StreamObserver<com.example.token.NodeServiceOuterClass.Empty> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(METHOD_TOKEN_DELIVERY_DATA, getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void tokenDeliveryDelete(com.example.token.NodeServiceOuterClass.TokenDelete request,
        io.grpc.stub.StreamObserver<com.example.token.NodeServiceOuterClass.Empty> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(METHOD_TOKEN_DELIVERY_DELETE, getCallOptions()), request, responseObserver);
    }
  }

  /**
   */
  public static final class NodeServiceBlockingStub extends io.grpc.stub.AbstractStub<NodeServiceBlockingStub> {
    private NodeServiceBlockingStub(io.grpc.Channel channel) {
      super(channel);
    }

    private NodeServiceBlockingStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected NodeServiceBlockingStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new NodeServiceBlockingStub(channel, callOptions);
    }

    /**
     */
    public com.example.token.NodeServiceOuterClass.JoinResponse joinNetwork(com.example.token.NodeServiceOuterClass.JoinRequest request) {
      return blockingUnaryCall(
          getChannel(), METHOD_JOIN_NETWORK, getCallOptions(), request);
    }

    /**
     */
    public com.example.token.NodeServiceOuterClass.Empty tokenDeliveryData(com.example.token.NodeServiceOuterClass.TokenData request) {
      return blockingUnaryCall(
          getChannel(), METHOD_TOKEN_DELIVERY_DATA, getCallOptions(), request);
    }

    /**
     */
    public com.example.token.NodeServiceOuterClass.Empty tokenDeliveryDelete(com.example.token.NodeServiceOuterClass.TokenDelete request) {
      return blockingUnaryCall(
          getChannel(), METHOD_TOKEN_DELIVERY_DELETE, getCallOptions(), request);
    }
  }

  /**
   */
  public static final class NodeServiceFutureStub extends io.grpc.stub.AbstractStub<NodeServiceFutureStub> {
    private NodeServiceFutureStub(io.grpc.Channel channel) {
      super(channel);
    }

    private NodeServiceFutureStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected NodeServiceFutureStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new NodeServiceFutureStub(channel, callOptions);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.example.token.NodeServiceOuterClass.JoinResponse> joinNetwork(
        com.example.token.NodeServiceOuterClass.JoinRequest request) {
      return futureUnaryCall(
          getChannel().newCall(METHOD_JOIN_NETWORK, getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.example.token.NodeServiceOuterClass.Empty> tokenDeliveryData(
        com.example.token.NodeServiceOuterClass.TokenData request) {
      return futureUnaryCall(
          getChannel().newCall(METHOD_TOKEN_DELIVERY_DATA, getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.example.token.NodeServiceOuterClass.Empty> tokenDeliveryDelete(
        com.example.token.NodeServiceOuterClass.TokenDelete request) {
      return futureUnaryCall(
          getChannel().newCall(METHOD_TOKEN_DELIVERY_DELETE, getCallOptions()), request);
    }
  }

  private static final int METHODID_JOIN_NETWORK = 0;
  private static final int METHODID_TOKEN_DELIVERY_DATA = 1;
  private static final int METHODID_TOKEN_DELIVERY_DELETE = 2;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final NodeServiceImplBase serviceImpl;
    private final int methodId;

    MethodHandlers(NodeServiceImplBase serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_JOIN_NETWORK:
          serviceImpl.joinNetwork((com.example.token.NodeServiceOuterClass.JoinRequest) request,
              (io.grpc.stub.StreamObserver<com.example.token.NodeServiceOuterClass.JoinResponse>) responseObserver);
          break;
        case METHODID_TOKEN_DELIVERY_DATA:
          serviceImpl.tokenDeliveryData((com.example.token.NodeServiceOuterClass.TokenData) request,
              (io.grpc.stub.StreamObserver<com.example.token.NodeServiceOuterClass.Empty>) responseObserver);
          break;
        case METHODID_TOKEN_DELIVERY_DELETE:
          serviceImpl.tokenDeliveryDelete((com.example.token.NodeServiceOuterClass.TokenDelete) request,
              (io.grpc.stub.StreamObserver<com.example.token.NodeServiceOuterClass.Empty>) responseObserver);
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

  private static abstract class NodeServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    NodeServiceBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return com.example.token.NodeServiceOuterClass.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("NodeService");
    }
  }

  private static final class NodeServiceFileDescriptorSupplier
      extends NodeServiceBaseDescriptorSupplier {
    NodeServiceFileDescriptorSupplier() {}
  }

  private static final class NodeServiceMethodDescriptorSupplier
      extends NodeServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final String methodName;

    NodeServiceMethodDescriptorSupplier(String methodName) {
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
      synchronized (NodeServiceGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new NodeServiceFileDescriptorSupplier())
              .addMethod(METHOD_JOIN_NETWORK)
              .addMethod(METHOD_TOKEN_DELIVERY_DATA)
              .addMethod(METHOD_TOKEN_DELIVERY_DELETE)
              .build();
        }
      }
    }
    return result;
  }
}
