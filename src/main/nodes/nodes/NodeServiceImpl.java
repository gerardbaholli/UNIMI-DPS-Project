package nodes;

import com.example.token.NodeServiceGrpc.NodeServiceImplBase;
import com.example.token.NodeServiceOuterClass.JoinRequest;
import com.example.token.NodeServiceOuterClass.JoinResponse;
import com.example.token.NodeServiceOuterClass.TokenResponse;
import com.example.token.NodeServiceOuterClass.TokenMessage;
import io.grpc.stub.StreamObserver;

public class NodeServiceImpl extends NodeServiceImplBase {

    @Override
    public void joinNetwork(JoinRequest joinRequest,
                            StreamObserver<JoinResponse> joinResponseStreamObserver){

        System.out.println(joinRequest);

        JoinResponse joinResponse = JoinResponse.newBuilder()
                .setIp("localhost")
                .setPort(8080)
                .setMessage("Ok").build();

         joinResponseStreamObserver.onNext(joinResponse);

         // joinResponseStreamObserver.onCompleted();

    }

    @Override
    public void token(TokenMessage tokenMessage,
                      StreamObserver<TokenResponse> tokenMessageStreamObserver){

        System.out.println(tokenMessage);

        TokenResponse tokenResponse = TokenResponse.newBuilder()
                .setAck("Received ack from node " + tokenMessage.getTo()).build();

        tokenMessageStreamObserver.onNext(tokenResponse);

        // tokenMessageStreamObserver.onCompleted();

    }


}
