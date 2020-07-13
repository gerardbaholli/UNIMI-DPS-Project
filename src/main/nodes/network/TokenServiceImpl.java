package network;

import com.example.token2.TokenServiceGrpc;
import com.example.token2.TokenServiceGrpc.TokenServiceStub;
import com.example.token2.TokenServiceGrpc.TokenServiceImplBase;
import com.example.token2.TokenServiceOuterClass.Empty;
import com.example.token2.TokenServiceOuterClass.Token;
import com.example.token2.TokenServiceOuterClass.JoinResponse;
import com.example.token2.TokenServiceOuterClass.JoinRequest;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;


public class TokenServiceImpl extends TokenServiceImplBase {



    @Override
    public synchronized void joinNetwork(JoinRequest joinRequest, StreamObserver<JoinResponse> joinResponseStreamObserver) {

        System.out.println("Ehy, I'm node " + joinRequest.getId() +
                " - " + joinRequest.getPort() + " join me as your target.");

        JoinResponse joinResponse = JoinResponse.newBuilder()
                .setId(Node.getInstance().getTargetId())
                .setIpAddress(Node.getInstance().getTargetIpAddress())
                .setPort(Node.getInstance().getTargetPort())
                .setMessage("success").build();

        // update the target of the node receiving the join request
        Node.getInstance().setTargetId(joinRequest.getId());
        Node.getInstance().setTargetIpAddress(joinRequest.getIpAddress());
        Node.getInstance().setTargetPort(joinRequest.getPort());

        joinResponseStreamObserver.onNext(joinResponse);
        joinResponseStreamObserver.onCompleted();

    }

    @Override
    public void tokenDelivery(Token token, StreamObserver<Empty> response) {

        Empty empty = Empty.newBuilder().build();
        response.onNext(empty);
        // response.onCompleted();

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("Token: " + token.getValue());

        ManagedChannel channel = ManagedChannelBuilder
                .forTarget(Node.getInstance().getTargetIpAddress() + ":" + Node.getInstance().getTargetPort())
                .usePlaintext(true).build();

        TokenServiceStub stub = TokenServiceGrpc.newStub(channel);

        int i = token.getValue() + 1;

        Token newToken = Token.newBuilder().setValue(i).build();

        stub.tokenDelivery(newToken, new StreamObserver<Empty>() {
            @Override
            public void onNext(Empty empty) {
                System.out.println("Ciao");
            }

            @Override
            public void onError(Throwable throwable) {
                System.out.println("Error - " + throwable.toString());
            }

            @Override
            public void onCompleted() {
                System.out.println("Token inviato");
            }
        });


    }


}
