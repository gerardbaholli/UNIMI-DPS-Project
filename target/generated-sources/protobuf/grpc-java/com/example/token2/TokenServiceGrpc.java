package com.example.token2;

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
    comments = "Source: TokenService.proto")
public final class TokenServiceGrpc {

  private TokenServiceGrpc() {}

  public static final String SERVICE_NAME = "com.example.token2.TokenService";

  // Static method descriptors that strictly reflect the proto.
  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  public static final io.grpc.MethodDescriptor<com.example.token2.TokenServiceOuterClass.JoinRequest,
      com.example.token2.TokenServiceOuterClass.JoinResponse> METHOD_JOIN_NETWORK =
      io.grpc.MethodDescriptor.<com.example.token2.TokenServiceOuterClass.JoinRequest, com.example.token2.TokenServiceOuterClass.JoinResponse>newBuilder()
          .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
          .setFullMethodName(generateFullMethodName(
              "com.example.token2.TokenService", "joinNetwork"))
          .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
              com.example.token2.TokenServiceOuterClass.JoinRequest.getDefaultInstance()))
          .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
              com.example.token2.TokenServiceOuterClass.JoinResponse.getDefaultInstance()))
          .setSchemaDescriptor(new TokenServiceMethodDescriptorSupplier("joinNetwork"))
          .build();
  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  public static final io.grpc.MethodDescriptor<com.example.token2.TokenServiceOuterClass.Token,
      com.example.token2.TokenServiceOuterClass.Empty> METHOD_TOKEN_DELIVERY =
      io.grpc.MethodDescriptor.<com.example.token2.TokenServiceOuterClass.Token, com.example.token2.TokenServiceOuterClass.Empty>newBuilder()
          .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
          .setFullMethodName(generateFullMethodName(
              "com.example.token2.TokenService", "tokenDelivery"))
          .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
              com.example.token2.TokenServiceOuterClass.Token.getDefaultInstance()))
          .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
              com.example.token2.TokenServiceOuterClass.Empty.getDefaultInstance()))
          .setSchemaDescriptor(new TokenServiceMethodDescriptorSupplier("tokenDelivery"))
          .build();

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static TokenServiceStub newStub(io.grpc.Channel channel) {
    return new TokenServiceStub(channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static TokenServiceBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    return new TokenServiceBlockingStub(channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static TokenServiceFutureStub newFutureStub(
      io.grpc.Channel channel) {
    return new TokenServiceFutureStub(channel);
  }

  /**
   */
  public static abstract class TokenServiceImplBase implements io.grpc.BindableService {

    /**
     */
    public void joinNetwork(com.example.token2.TokenServiceOuterClass.JoinRequest request,
        io.grpc.stub.StreamObserver<com.example.token2.TokenServiceOuterClass.JoinResponse> responseObserver) {
      asyncUnimplementedUnaryCall(METHOD_JOIN_NETWORK, responseObserver);
    }

    /**
     */
    public void tokenDelivery(com.example.token2.TokenServiceOuterClass.Token request,
        io.grpc.stub.StreamObserver<com.example.token2.TokenServiceOuterClass.Empty> responseObserver) {
      asyncUnimplementedUnaryCall(METHOD_TOKEN_DELIVERY, responseObserver);
    }

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
          .addMethod(
            METHOD_JOIN_NETWORK,
            asyncUnaryCall(
              new MethodHandlers<
                com.example.token2.TokenServiceOuterClass.JoinRequest,
                com.example.token2.TokenServiceOuterClass.JoinResponse>(
                  this, METHODID_JOIN_NETWORK)))
          .addMethod(
            METHOD_TOKEN_DELIVERY,
            asyncUnaryCall(
              new MethodHandlers<
                com.example.token2.TokenServiceOuterClass.Token,
                com.example.token2.TokenServiceOuterClass.Empty>(
                  this, METHODID_TOKEN_DELIVERY)))
          .build();
    }
  }

  /**
   */
  public static final class TokenServiceStub extends io.grpc.stub.AbstractStub<TokenServiceStub> {
    private TokenServiceStub(io.grpc.Channel channel) {
      super(channel);
    }

    private TokenServiceStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected TokenServiceStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new TokenServiceStub(channel, callOptions);
    }

    /**
     */
    public void joinNetwork(com.example.token2.TokenServiceOuterClass.JoinRequest request,
        io.grpc.stub.StreamObserver<com.example.token2.TokenServiceOuterClass.JoinResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(METHOD_JOIN_NETWORK, getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void tokenDelivery(com.example.token2.TokenServiceOuterClass.Token request,
        io.grpc.stub.StreamObserver<com.example.token2.TokenServiceOuterClass.Empty> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(METHOD_TOKEN_DELIVERY, getCallOptions()), request, responseObserver);
    }
  }

  /**
   */
  public static final class TokenServiceBlockingStub extends io.grpc.stub.AbstractStub<TokenServiceBlockingStub> {
    private TokenServiceBlockingStub(io.grpc.Channel channel) {
      super(channel);
    }

    private TokenServiceBlockingStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected TokenServiceBlockingStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new TokenServiceBlockingStub(channel, callOptions);
    }

    /**
     */
    public com.example.token2.TokenServiceOuterClass.JoinResponse joinNetwork(com.example.token2.TokenServiceOuterClass.JoinRequest request) {
      return blockingUnaryCall(
          getChannel(), METHOD_JOIN_NETWORK, getCallOptions(), request);
    }

    /**
     */
    public com.example.token2.TokenServiceOuterClass.Empty tokenDelivery(com.example.token2.TokenServiceOuterClass.Token request) {
      return blockingUnaryCall(
          getChannel(), METHOD_TOKEN_DELIVERY, getCallOptions(), request);
    }
  }

  /**
   */
  public static final class TokenServiceFutureStub extends io.grpc.stub.AbstractStub<TokenServiceFutureStub> {
    private TokenServiceFutureStub(io.grpc.Channel channel) {
      super(channel);
    }

    private TokenServiceFutureStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected TokenServiceFutureStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new TokenServiceFutureStub(channel, callOptions);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.example.token2.TokenServiceOuterClass.JoinResponse> joinNetwork(
        com.example.token2.TokenServiceOuterClass.JoinRequest request) {
      return futureUnaryCall(
          getChannel().newCall(METHOD_JOIN_NETWORK, getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.example.token2.TokenServiceOuterClass.Empty> tokenDelivery(
        com.example.token2.TokenServiceOuterClass.Token request) {
      return futureUnaryCall(
          getChannel().newCall(METHOD_TOKEN_DELIVERY, getCallOptions()), request);
    }
  }

  private static final int METHODID_JOIN_NETWORK = 0;
  private static final int METHODID_TOKEN_DELIVERY = 1;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final TokenServiceImplBase serviceImpl;
    private final int methodId;

    MethodHandlers(TokenServiceImplBase serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_JOIN_NETWORK:
          serviceImpl.joinNetwork((com.example.token2.TokenServiceOuterClass.JoinRequest) request,
              (io.grpc.stub.StreamObserver<com.example.token2.TokenServiceOuterClass.JoinResponse>) responseObserver);
          break;
        case METHODID_TOKEN_DELIVERY:
          serviceImpl.tokenDelivery((com.example.token2.TokenServiceOuterClass.Token) request,
              (io.grpc.stub.StreamObserver<com.example.token2.TokenServiceOuterClass.Empty>) responseObserver);
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

  private static abstract class TokenServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    TokenServiceBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return com.example.token2.TokenServiceOuterClass.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("TokenService");
    }
  }

  private static final class TokenServiceFileDescriptorSupplier
      extends TokenServiceBaseDescriptorSupplier {
    TokenServiceFileDescriptorSupplier() {}
  }

  private static final class TokenServiceMethodDescriptorSupplier
      extends TokenServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final String methodName;

    TokenServiceMethodDescriptorSupplier(String methodName) {
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
      synchronized (TokenServiceGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new TokenServiceFileDescriptorSupplier())
              .addMethod(METHOD_JOIN_NETWORK)
              .addMethod(METHOD_TOKEN_DELIVERY)
              .build();
        }
      }
    }
    return result;
  }
}
