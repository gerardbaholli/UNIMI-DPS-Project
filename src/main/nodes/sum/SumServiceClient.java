package sum;

import com.example.sum.SumServiceGrpc;
import com.example.sum.SumServiceGrpc.*;
import com.example.sum.SumServiceOuterClass.*;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;

import java.util.concurrent.TimeUnit;

public class SumServiceClient {

    public static void main(String[] args) throws InterruptedException {

        System.out.println("Trying to call SimpleSum synchronous method:\n");

        synchronousCall();

        System.out.println("Done synchronous!");
        System.out.println("--------------");

        asynchronousStreamCall();

        System.out.println("Done asynchronous!");

    }


    public static void synchronousCall(){

        final ManagedChannel channel = ManagedChannelBuilder.forTarget("localhost:9999")
                .usePlaintext(true).build();

        SumServiceBlockingStub stub = SumServiceGrpc.newBlockingStub(channel);

        SumRequest request = SumRequest.newBuilder().setNum1(1).setNum2(2).build();

        SumResponse result = stub.simpleSum(request);

        System.out.println(result.getResult());

        channel.shutdown();

    }


    public static void asynchronousStreamCall() throws InterruptedException {

        final ManagedChannel channel = ManagedChannelBuilder.forTarget("localhost:9999")
                .usePlaintext(true).build();

        // no blocking stub in asynchronous
        SumServiceStub stub = SumServiceGrpc.newStub(channel);

        // FIRST STREAM
        SumRequest request1 = SumRequest.newBuilder()
                .setNum1(2).setNum2(2).build();

        stub.streamSum(request1, new StreamObserver<SumResponse>() {
            @Override
            public void onNext(SumResponse sumResponse) {
                System.out.println(sumResponse.getResult());
            }
            @Override
            public void onError(Throwable throwable) {
                System.out.println("Error: " + throwable.getMessage());
            }
            @Override
            public void onCompleted() {
                channel.shutdown();
            }
        });

        // SECOND STREAM
        SumRequest request2 = SumRequest.newBuilder()
                .setNum1(2).setNum2(3).build();

        stub.streamSum(request2, new StreamObserver<SumResponse>() {
            @Override
            public void onNext(SumResponse sumResponse) {
                System.out.println(sumResponse.getResult());
            }
            @Override
            public void onError(Throwable throwable) {
                System.out.println("Error: " + throwable.getMessage());
            }
            @Override
            public void onCompleted() {
                channel.shutdown();
            }
        });

        channel.awaitTermination(10, TimeUnit.SECONDS);

    }

}
