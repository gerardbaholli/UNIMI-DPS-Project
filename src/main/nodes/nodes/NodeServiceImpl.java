package nodes;

import com.example.token.NodeServiceGrpc.NodeServiceImplBase;
import com.example.token.NodeServiceOuterClass.TokenData;
import com.example.token.NodeServiceOuterClass.TokenData.WaitingList;
import com.example.token.NodeServiceOuterClass.Empty;
import com.example.token.NodeServiceOuterClass.JoinRequest;
import com.example.token.NodeServiceOuterClass.JoinResponse;
import io.grpc.stub.StreamObserver;


public class NodeServiceImpl extends NodeServiceImplBase {


    // service called to join the network
    // synchronized to prevent to prevent two nodes joining at the same time
    @Override
    public synchronized void joinNetwork(JoinRequest joinRequest,
                            StreamObserver<JoinResponse> joinResponseStreamObserver){

        System.out.println("Join Request:\n" + "Ehy, I'm node " +
                joinRequest.getId() + " - " + joinRequest.getPort() +
                " join me as your target.");


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

        System.out.println(
                "Ready list:\n" + request.getReadyListList() + "\n" +
                "Waiting list:\n" + request.getWaitingListList());


        for (WaitingList value : request.getWaitingListList()){
            System.out.println(value);
        }


        Empty response = Empty.newBuilder().build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();

    }

}
