package nodes;

import com.example.token.NodeServiceGrpc;
import com.example.token.NodeServiceGrpc.NodeServiceImplBase;
import com.example.token.NodeServiceOuterClass.TokenData;
import com.example.token.NodeServiceOuterClass.Empty;
import com.example.token.NodeServiceOuterClass.JoinRequest;
import com.example.token.NodeServiceOuterClass.JoinResponse;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;


public class NodeServiceImpl extends NodeServiceImplBase {

    Node node;

    public NodeServiceImpl(Node node){
        this.node = node;
    }

    // service called to join the network
    // synchronized to prevent to prevent two nodes joining at the same time
    @Override
    public synchronized void joinNetwork(JoinRequest joinRequest,
                            StreamObserver<JoinResponse> joinResponseStreamObserver){

        System.out.println("Ehy, I'm node " + joinRequest.getId() +
                " - " + joinRequest.getPort() + " join me as your target.");

        JoinResponse joinResponse = JoinResponse.newBuilder()
                .setId(TargetNode.getInstance().getTargetId())
                .setIpAddress(TargetNode.getInstance().getTargetIpAddress())
                .setPort(TargetNode.getInstance().getTargetPort())
                .setMessage("success").build();

        // update the target of the node receiving the join request
        TargetNode.getInstance().setTargetId(joinRequest.getId());
        TargetNode.getInstance().setTargetIpAddress(joinRequest.getIpAddress());
        TargetNode.getInstance().setTargetPort(joinRequest.getPort());

         joinResponseStreamObserver.onNext(joinResponse);
         joinResponseStreamObserver.onCompleted();

    }



    @Override
    public synchronized void tokenDelivery(TokenData request,
                          StreamObserver<Empty> responseObserver) {

        Empty response = Empty.newBuilder().build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();


        ManagedChannel channel = ManagedChannelBuilder
                .forTarget(
                        TargetNode.getInstance().getTargetIpAddress() + ":" +
                                TargetNode.getInstance().getTargetPort())
                .usePlaintext(true).build();


        TokenData newRequest = request.toBuilder()
                .addReady(TokenData.Ready.newBuilder()
                    .setId(1)
                    .setValue(1.2).build())
                .addWaiting(TokenData.Waiting.newBuilder()
                        .setId(1).build())
                .addWaiting(TokenData.Waiting.newBuilder()
                    .setId(2).build())
                .build();

        System.out.println("OK 1");

        System.out.println(
                "Ready list:\n" + newRequest.getReadyList() + "\n" +
                "Waiting list:\n" + newRequest.getWaitingList());

        System.out.println("OK 2");

        System.out.println("Mando token a " + TargetNode.getInstance().getTargetId());

        // creating a blocking stub on the channel
        NodeServiceGrpc.NodeServiceBlockingStub stub = NodeServiceGrpc.newBlockingStub(channel);

        // send the token to the target node
        stub.tokenDelivery(newRequest);

        channel.shutdown();


    }

}
