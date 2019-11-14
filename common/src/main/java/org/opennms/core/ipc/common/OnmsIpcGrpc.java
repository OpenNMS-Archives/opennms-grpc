package org.opennms.core.ipc.common;

import static io.grpc.MethodDescriptor.generateFullMethodName;
import static io.grpc.stub.ClientCalls.asyncBidiStreamingCall;
import static io.grpc.stub.ServerCalls.asyncBidiStreamingCall;
import static io.grpc.stub.ServerCalls.asyncUnimplementedStreamingCall;

/**
 * <pre>
 * service definition for RpcMessage.
 * </pre>
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.24.0)",
    comments = "Source: ipc.proto")
public final class OnmsIpcGrpc {

  private OnmsIpcGrpc() {}

  public static final String SERVICE_NAME = "OnmsIpc";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<org.opennms.core.ipc.common.RpcMessage,
      org.opennms.core.ipc.common.RpcMessage> getRpcStreamingMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "RpcStreaming",
      requestType = org.opennms.core.ipc.common.RpcMessage.class,
      responseType = org.opennms.core.ipc.common.RpcMessage.class,
      methodType = io.grpc.MethodDescriptor.MethodType.BIDI_STREAMING)
  public static io.grpc.MethodDescriptor<org.opennms.core.ipc.common.RpcMessage,
      org.opennms.core.ipc.common.RpcMessage> getRpcStreamingMethod() {
    io.grpc.MethodDescriptor<org.opennms.core.ipc.common.RpcMessage, org.opennms.core.ipc.common.RpcMessage> getRpcStreamingMethod;
    if ((getRpcStreamingMethod = OnmsIpcGrpc.getRpcStreamingMethod) == null) {
      synchronized (OnmsIpcGrpc.class) {
        if ((getRpcStreamingMethod = OnmsIpcGrpc.getRpcStreamingMethod) == null) {
          OnmsIpcGrpc.getRpcStreamingMethod = getRpcStreamingMethod =
              io.grpc.MethodDescriptor.<org.opennms.core.ipc.common.RpcMessage, org.opennms.core.ipc.common.RpcMessage>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.BIDI_STREAMING)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "RpcStreaming"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  org.opennms.core.ipc.common.RpcMessage.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  org.opennms.core.ipc.common.RpcMessage.getDefaultInstance()))
              .setSchemaDescriptor(new OnmsIpcMethodDescriptorSupplier("RpcStreaming"))
              .build();
        }
      }
    }
    return getRpcStreamingMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static OnmsIpcStub newStub(io.grpc.Channel channel) {
    return new OnmsIpcStub(channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static OnmsIpcBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    return new OnmsIpcBlockingStub(channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static OnmsIpcFutureStub newFutureStub(
      io.grpc.Channel channel) {
    return new OnmsIpcFutureStub(channel);
  }

  /**
   * <pre>
   * service definition for RpcMessage.
   * </pre>
   */
  public static abstract class OnmsIpcImplBase implements io.grpc.BindableService {

    /**
     * <pre>
     * Streams Rpc messages between Minion and OpenNMS.
     * </pre>
     */
    public io.grpc.stub.StreamObserver<org.opennms.core.ipc.common.RpcMessage> rpcStreaming(
        io.grpc.stub.StreamObserver<org.opennms.core.ipc.common.RpcMessage> responseObserver) {
      return asyncUnimplementedStreamingCall(getRpcStreamingMethod(), responseObserver);
    }

    @Override public final io.grpc.ServerServiceDefinition bindService() {
      return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
          .addMethod(
            getRpcStreamingMethod(),
            asyncBidiStreamingCall(
              new MethodHandlers<
                org.opennms.core.ipc.common.RpcMessage,
                org.opennms.core.ipc.common.RpcMessage>(
                  this, METHODID_RPC_STREAMING)))
          .build();
    }
  }

  /**
   * <pre>
   * service definition for RpcMessage.
   * </pre>
   */
  public static final class OnmsIpcStub extends io.grpc.stub.AbstractStub<OnmsIpcStub> {
    private OnmsIpcStub(io.grpc.Channel channel) {
      super(channel);
    }

    private OnmsIpcStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @Override
    protected OnmsIpcStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new OnmsIpcStub(channel, callOptions);
    }

    /**
     * <pre>
     * Streams Rpc messages between Minion and OpenNMS.
     * </pre>
     */
    public io.grpc.stub.StreamObserver<org.opennms.core.ipc.common.RpcMessage> rpcStreaming(
        io.grpc.stub.StreamObserver<org.opennms.core.ipc.common.RpcMessage> responseObserver) {
      return asyncBidiStreamingCall(
          getChannel().newCall(getRpcStreamingMethod(), getCallOptions()), responseObserver);
    }
  }

  /**
   * <pre>
   * service definition for RpcMessage.
   * </pre>
   */
  public static final class OnmsIpcBlockingStub extends io.grpc.stub.AbstractStub<OnmsIpcBlockingStub> {
    private OnmsIpcBlockingStub(io.grpc.Channel channel) {
      super(channel);
    }

    private OnmsIpcBlockingStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @Override
    protected OnmsIpcBlockingStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new OnmsIpcBlockingStub(channel, callOptions);
    }
  }

  /**
   * <pre>
   * service definition for RpcMessage.
   * </pre>
   */
  public static final class OnmsIpcFutureStub extends io.grpc.stub.AbstractStub<OnmsIpcFutureStub> {
    private OnmsIpcFutureStub(io.grpc.Channel channel) {
      super(channel);
    }

    private OnmsIpcFutureStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @Override
    protected OnmsIpcFutureStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new OnmsIpcFutureStub(channel, callOptions);
    }
  }

  private static final int METHODID_RPC_STREAMING = 0;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final OnmsIpcImplBase serviceImpl;
    private final int methodId;

    MethodHandlers(OnmsIpcImplBase serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        default:
          throw new AssertionError();
      }
    }

    @Override
    @SuppressWarnings("unchecked")
    public io.grpc.stub.StreamObserver<Req> invoke(
        io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_RPC_STREAMING:
          return (io.grpc.stub.StreamObserver<Req>) serviceImpl.rpcStreaming(
              (io.grpc.stub.StreamObserver<org.opennms.core.ipc.common.RpcMessage>) responseObserver);
        default:
          throw new AssertionError();
      }
    }
  }

  private static abstract class OnmsIpcBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    OnmsIpcBaseDescriptorSupplier() {}

    @Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return org.opennms.core.ipc.common.RpcProto.getDescriptor();
    }

    @Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("OnmsIpc");
    }
  }

  private static final class OnmsIpcFileDescriptorSupplier
      extends OnmsIpcBaseDescriptorSupplier {
    OnmsIpcFileDescriptorSupplier() {}
  }

  private static final class OnmsIpcMethodDescriptorSupplier
      extends OnmsIpcBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final String methodName;

    OnmsIpcMethodDescriptorSupplier(String methodName) {
      this.methodName = methodName;
    }

    @Override
    public com.google.protobuf.Descriptors.MethodDescriptor getMethodDescriptor() {
      return getServiceDescriptor().findMethodByName(methodName);
    }
  }

  private static volatile io.grpc.ServiceDescriptor serviceDescriptor;

  public static io.grpc.ServiceDescriptor getServiceDescriptor() {
    io.grpc.ServiceDescriptor result = serviceDescriptor;
    if (result == null) {
      synchronized (OnmsIpcGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new OnmsIpcFileDescriptorSupplier())
              .addMethod(getRpcStreamingMethod())
              .build();
        }
      }
    }
    return result;
  }
}
