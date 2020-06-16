package nodes;

import com.example.token.NodeServiceGrpc.NodeServiceImplBase;
import com.example.token.NodeServiceOuterClass.JoinRequest;
import com.example.token.NodeServiceOuterClass.JoinResponse;
import com.example.token.NodeServiceOuterClass.TokenResponse;
import com.example.token.NodeServiceOuterClass.TokenMessage;
import io.grpc.stub.StreamObserver;

public class NodeServiceImpl extends NodeServiceImplBase {

    TargetNode targetNode;

    public NodeServiceImpl(TargetNode targetNode) {
        this.targetNode = targetNode;
    }

    // service called to join the network
    // synchronized to prevent to prevent two nodes joining at the same time
    @Override
    public synchronized void joinNetwork(JoinRequest joinRequest,
                            StreamObserver<JoinResponse> joinResponseStreamObserver){

        System.out.println("Join Request:\n" +
                joinRequest.getId() + " " +
                joinRequest.getIpAddress() + " " +
                joinRequest.getPort());

        JoinResponse joinResponse = JoinResponse.newBuilder()
                .setId(targetNode.getTargetId())
                .setIpAddress(targetNode.getTargetIpAddress())
                .setPort(targetNode.getTargetPort())
                .setMessage("success").build();

         joinResponseStreamObserver.onNext(joinResponse);
         joinResponseStreamObserver.onCompleted();


    }

    @Override
    public void token(TokenMessage tokenMessage,
                      StreamObserver<TokenResponse> tokenMessageStreamObserver){

        System.out.println(tokenMessage);

        TokenResponse tokenResponse = TokenResponse.newBuilder()
                .setAck("Received ack from node " + tokenMessage.getTo()).build();

        tokenMessageStreamObserver.onNext(tokenResponse);

        tokenMessageStreamObserver.onCompleted();

    }


}
