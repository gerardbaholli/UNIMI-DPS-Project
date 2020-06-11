package sum;

import com.example.sum.SumServiceGrpc.SumServiceImplBase;
import com.example.sum.SumServiceOuterClass.*;
import io.grpc.stub.StreamObserver;

public class SumServiceImpl extends SumServiceImplBase {

    @Override
    public void simpleSum(SumRequest request, StreamObserver<SumResponse> responseObserver) {

        System.out.println(request);

        SumResponse response = SumResponse.newBuilder()
                .setResult(request.getNum1()+request.getNum2())
                .build();

        responseObserver.onNext(response);

        responseObserver.onCompleted();
    }

    @Override
    public void streamSum(SumRequest request, StreamObserver<SumResponse> responseObserver) {

        System.out.println("Stream method called:");
        System.out.println(request);

        SumResponse response = SumResponse.newBuilder()
                .setResult(request.getNum1()+request.getNum2())
                .build();

        responseObserver.onNext(response);

        responseObserver.onCompleted();

    }
}
