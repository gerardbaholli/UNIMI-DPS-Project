package com.example.sum;

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
    comments = "Source: SumService.proto")
public final class SumServiceGrpc {

  private SumServiceGrpc() {}

  public static final String SERVICE_NAME = "com.example.sum.SumService";

  // Static method descriptors that strictly reflect the proto.
  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  public static final io.grpc.MethodDescriptor<com.example.sum.SumServiceOuterClass.SumRequest,
      com.example.sum.SumServiceOuterClass.SumResponse> METHOD_SIMPLE_SUM =
      io.grpc.MethodDescriptor.<com.example.sum.SumServiceOuterClass.SumRequest, com.example.sum.SumServiceOuterClass.SumResponse>newBuilder()
          .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
          .setFullMethodName(generateFullMethodName(
              "com.example.sum.SumService", "simpleSum"))
          .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
              com.example.sum.SumServiceOuterClass.SumRequest.getDefaultInstance()))
          .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
              com.example.sum.SumServiceOuterClass.SumResponse.getDefaultInstance()))
          .setSchemaDescriptor(new SumServiceMethodDescriptorSupplier("simpleSum"))
          .build();
  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  public static final io.grpc.MethodDescriptor<com.example.sum.SumServiceOuterClass.SumRequest,
      com.example.sum.SumServiceOuterClass.SumResponse> METHOD_STREAM_SUM =
      io.grpc.MethodDescriptor.<com.example.sum.SumServiceOuterClass.SumRequest, com.example.sum.SumServiceOuterClass.SumResponse>newBuilder()
          .setType(io.grpc.MethodDescriptor.MethodType.SERVER_STREAMING)
          .setFullMethodName(generateFullMethodName(
              "com.example.sum.SumService", "streamSum"))
          .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
              com.example.sum.SumServiceOuterClass.SumRequest.getDefaultInstance()))
          .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
              com.example.sum.SumServiceOuterClass.SumResponse.getDefaultInstance()))
          .setSchemaDescriptor(new SumServiceMethodDescriptorSupplier("streamSum"))
          .build();

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static SumServiceStub newStub(io.grpc.Channel channel) {
    return new SumServiceStub(channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static SumServiceBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    return new SumServiceBlockingStub(channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static SumServiceFutureStub newFutureStub(
      io.grpc.Channel channel) {
    return new SumServiceFutureStub(channel);
  }

  /**
   */
  public static abstract class SumServiceImplBase implements io.grpc.BindableService {

    /**
     */
    public void simpleSum(com.example.sum.SumServiceOuterClass.SumRequest request,
        io.grpc.stub.StreamObserver<com.example.sum.SumServiceOuterClass.SumResponse> responseObserver) {
      asyncUnimplementedUnaryCall(METHOD_SIMPLE_SUM, responseObserver);
    }

    /**
     */
    public void streamSum(com.example.sum.SumServiceOuterClass.SumRequest request,
        io.grpc.stub.StreamObserver<com.example.sum.SumServiceOuterClass.SumResponse> responseObserver) {
      asyncUnimplementedUnaryCall(METHOD_STREAM_SUM, responseObserver);
    }

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
          .addMethod(
            METHOD_SIMPLE_SUM,
            asyncUnaryCall(
              new MethodHandlers<
                com.example.sum.SumServiceOuterClass.SumRequest,
                com.example.sum.SumServiceOuterClass.SumResponse>(
                  this, METHODID_SIMPLE_SUM)))
          .addMethod(
            METHOD_STREAM_SUM,
            asyncServerStreamingCall(
              new MethodHandlers<
                com.example.sum.SumServiceOuterClass.SumRequest,
                com.example.sum.SumServiceOuterClass.SumResponse>(
                  this, METHODID_STREAM_SUM)))
          .build();
    }
  }

  /**
   */
  public static final class SumServiceStub extends io.grpc.stub.AbstractStub<SumServiceStub> {
    private SumServiceStub(io.grpc.Channel channel) {
      super(channel);
    }

    private SumServiceStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected SumServiceStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new SumServiceStub(channel, callOptions);
    }

    /**
     */
    public void simpleSum(com.example.sum.SumServiceOuterClass.SumRequest request,
        io.grpc.stub.StreamObserver<com.example.sum.SumServiceOuterClass.SumResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(METHOD_SIMPLE_SUM, getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void streamSum(com.example.sum.SumServiceOuterClass.SumRequest request,
        io.grpc.stub.StreamObserver<com.example.sum.SumServiceOuterClass.SumResponse> responseObserver) {
      asyncServerStreamingCall(
          getChannel().newCall(METHOD_STREAM_SUM, getCallOptions()), request, responseObserver);
    }
  }

  /**
   */
  public static final class SumServiceBlockingStub extends io.grpc.stub.AbstractStub<SumServiceBlockingStub> {
    private SumServiceBlockingStub(io.grpc.Channel channel) {
      super(channel);
    }

    private SumServiceBlockingStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected SumServiceBlockingStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new SumServiceBlockingStub(channel, callOptions);
    }

    /**
     */
    public com.example.sum.SumServiceOuterClass.SumResponse simpleSum(com.example.sum.SumServiceOuterClass.SumRequest request) {
      return blockingUnaryCall(
          getChannel(), METHOD_SIMPLE_SUM, getCallOptions(), request);
    }

    /**
     */
    public java.util.Iterator<com.example.sum.SumServiceOuterClass.SumResponse> streamSum(
        com.example.sum.SumServiceOuterClass.SumRequest request) {
      return blockingServerStreamingCall(
          getChannel(), METHOD_STREAM_SUM, getCallOptions(), request);
    }
  }

  /**
   */
  public static final class SumServiceFutureStub extends io.grpc.stub.AbstractStub<SumServiceFutureStub> {
    private SumServiceFutureStub(io.grpc.Channel channel) {
      super(channel);
    }

    private SumServiceFutureStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected SumServiceFutureStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new SumServiceFutureStub(channel, callOptions);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.example.sum.SumServiceOuterClass.SumResponse> simpleSum(
        com.example.sum.SumServiceOuterClass.SumRequest request) {
      return futureUnaryCall(
          getChannel().newCall(METHOD_SIMPLE_SUM, getCallOptions()), request);
    }
  }

  private static final int METHODID_SIMPLE_SUM = 0;
  private static final int METHODID_STREAM_SUM = 1;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final SumServiceImplBase serviceImpl;
    private final int methodId;

    MethodHandlers(SumServiceImplBase serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_SIMPLE_SUM:
          serviceImpl.simpleSum((com.example.sum.SumServiceOuterClass.SumRequest) request,
              (io.grpc.stub.StreamObserver<com.example.sum.SumServiceOuterClass.SumResponse>) responseObserver);
          break;
        case METHODID_STREAM_SUM:
          serviceImpl.streamSum((com.example.sum.SumServiceOuterClass.SumRequest) request,
              (io.grpc.stub.StreamObserver<com.example.sum.SumServiceOuterClass.SumResponse>) responseObserver);
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

  private static abstract class SumServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    SumServiceBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return com.example.sum.SumServiceOuterClass.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("SumService");
    }
  }

  private static final class SumServiceFileDescriptorSupplier
      extends SumServiceBaseDescriptorSupplier {
    SumServiceFileDescriptorSupplier() {}
  }

  private static final class SumServiceMethodDescriptorSupplier
      extends SumServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final String methodName;

    SumServiceMethodDescriptorSupplier(String methodName) {
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
      synchronized (SumServiceGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new SumServiceFileDescriptorSupplier())
              .addMethod(METHOD_SIMPLE_SUM)
              .addMethod(METHOD_STREAM_SUM)
              .build();
        }
      }
    }
    return result;
  }
}
