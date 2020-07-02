package nodes;

import com.example.token.NodeServiceGrpc;
import com.example.token.NodeServiceGrpc.NodeServiceImplBase;
import com.example.token.NodeServiceOuterClass.TokenData;
import com.example.token.NodeServiceOuterClass.TokenData.Ready;
import com.example.token.NodeServiceOuterClass.TokenData.Waiting;
import com.example.token.NodeServiceOuterClass.Empty;
import com.example.token.NodeServiceOuterClass.JoinRequest;
import com.example.token.NodeServiceOuterClass.JoinResponse;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;

import java.util.ArrayList;
import java.util.List;


public class NodeServiceImpl extends NodeServiceImplBase {

    Node node;

    public NodeServiceImpl(Node node) {
        this.node = node;
    }

    // service called to join the network
    // synchronized to prevent to prevent two nodes joining at the same time
    @Override
    public synchronized void joinNetwork(JoinRequest joinRequest,
                                         StreamObserver<JoinResponse> joinResponseStreamObserver) {

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
    public synchronized void tokenDelivery(TokenData tokenData,
                                           StreamObserver<Empty> responseObserver) {

        Empty response = Empty.newBuilder().build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();

        System.out.println("Arrivato token a nodo " + node.getId());


        ManagedChannel channel = ManagedChannelBuilder
                .forTarget(
                        TargetNode.getInstance().getTargetIpAddress() + ":" +
                                TargetNode.getInstance().getTargetPort())
                .usePlaintext(true).build();

        // creating a blocking stub on the channel
        NodeServiceGrpc.NodeServiceBlockingStub stub = NodeServiceGrpc.newBlockingStub(channel);


        TokenData newTokenData = tokenData.toBuilder().build();


        // CONTROLLA SE E' PRESENTE L'ID IN READY LIST
        Boolean insideReady = false;
        for (Ready item : tokenData.getReadyList()) {
            if (item.getId() == node.getId()) {
                insideReady = true;
            }
        }


        // CONTROLLA SE IL TOKEN E' PIENO IN CASO POSITIVO MANDA AL GATEWAY
        if ((tokenData.getReadyCount() == tokenData.getWaitingCount()) && insideReady) {
            double finalAvg = computeFinalAvg(tokenData);
            // TODO: INVIARE AL GATEWAY
            System.out.println("SENT TOKEN TO THE GATEWAY " + finalAvg);
            newTokenData = TokenData.newBuilder().build();
        }


        // CONTROLLA SE E' PRESENTE L'ID IN WAITING LIST
        Boolean insideWaiting = false;
        for (Waiting item : tokenData.getWaitingList()) {
            if (item.getId() == node.getId()) {
                insideWaiting = true;
            }
        }


        // SLEEP DA RIMUOVERE, MESSA PER RALLENTARE UN PO'
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }



        if (LocalAvgList.getInstance().getSize() >= 1) {

            System.out.println("Ok la mia statistica è pronta!");
            if (insideReady) {
                System.out.println("1");
                stub.tokenDelivery(tokenData);
                channel.shutdown();
            } else if (insideWaiting) {
                System.out.println("2");
                newTokenData = tokenData.toBuilder()
                        .addReady(TokenData.Ready.newBuilder()
                                .setId(node.getId())
                                .setValue(LocalAvgList.getInstance().getLastValue()))
                        .build();
                stub.tokenDelivery(newTokenData);
                channel.shutdown();
            } else if (!insideReady && !insideWaiting){
                newTokenData = tokenData.toBuilder()
                        .addReady(TokenData.Ready.newBuilder()
                                .setId(node.getId())
                                .setValue(LocalAvgList.getInstance().getLastValue()))
                        .addWaiting(TokenData.Waiting.newBuilder()
                                .setId(node.getId()))
                        .build();
                stub.tokenDelivery(newTokenData);
                channel.shutdown();
            }

        } else {

            System.out.println("La mia statistica non è pronta!");
            if (insideReady || insideWaiting) {
                System.out.println("3");
                stub.tokenDelivery(tokenData);
                channel.shutdown();
            } else if (!insideWaiting) {
                System.out.println("4");
                newTokenData = tokenData.toBuilder()
                        .addWaiting(TokenData.Waiting.newBuilder()
                                .setId(node.getId()).build())
                        .build();
                stub.tokenDelivery(newTokenData);
                channel.shutdown();
            }

        }


        System.out.println("Ready list:\n" + newTokenData.getReadyList());
        System.out.println("Waiting list:\n" + newTokenData.getWaitingList());
        System.out.println("Mando token a " + TargetNode.getInstance().getTargetId());


        channel.shutdown();

    }

    public double computeFinalAvg(TokenData tokenData) {

        double result = 0.0;
        int count = 0;

        for (Ready item : tokenData.getReadyList()) {
            if (item.getId() == node.getId()) {
                result += item.getValue();
                count++;
            }
        }

        return result / count;
    }


}
