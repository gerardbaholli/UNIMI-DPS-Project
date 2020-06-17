package nodes;

import com.example.token.NodeServiceGrpc;
import com.example.token.NodeServiceOuterClass.TokenData;
import com.example.token.NodeServiceOuterClass.Empty;
import com.example.token.NodeServiceOuterClass.JoinRequest;
import com.example.token.NodeServiceOuterClass.JoinResponse;
import com.example.token.NodeServiceGrpc.NodeServiceBlockingStub;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;


public class ClientGRPC extends Thread {

    private Node node;

    public ClientGRPC(Node node) {
        this.node = node;
    }

    @Override
    public void run() {
        System.out.println("Sending join request message to " +
                TargetNode.getInstance().getTargetId());

        syncJoinNet();

        System.out.println("Node " + node.getId() + " linked to node " +
                TargetNode.getInstance().getTargetId());

        // if is the first node then create the token
        if (node.getId() == TargetNode.getInstance().getTargetId()){
            createTokenData();
        }

        /*
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        */

        // TODO: SE IL TOKEN è STATO RICEVUTO
        // se il token è stato ricevuto ed ha la statistica pronta, fa addTokenData
        // se il token è stato ricevuto e non ha la statistica pronta, fa skipTokenData
        while (true) {
            if (LocalAvgList.getInstance().getSize()>1){
                addTokenData(LocalAvgList.getInstance().getLastValue());
            } else {
                skipTokenData();
            }
        }

    }


    public void syncJoinNet() {

        final ManagedChannel channel = ManagedChannelBuilder
                .forTarget(TargetNode.getInstance().getTargetIpAddress() + ":" + TargetNode.getInstance().getTargetPort())
                .usePlaintext(true).build();

        // creating a blocking stub on the channel
        NodeServiceBlockingStub stub = NodeServiceGrpc.newBlockingStub(channel);

        // creating the JoinRequest object which will be provided as input to the RPC method
        JoinRequest joinRequest = JoinRequest.newBuilder()
                .setId(node.getId())
                .setIpAddress(node.getIpAddress())
                .setPort(node.getPort())
                .build();

        // calling the method, it returns an instance of JoinResponse
        JoinResponse joinResponse = stub.joinNetwork(joinRequest);

        // save the new target node after the join
        TargetNode.getInstance().setTargetId(joinResponse.getId());
        TargetNode.getInstance().setTargetIpAddress(joinResponse.getIpAddress());
        TargetNode.getInstance().setTargetPort(joinResponse.getPort());

        // printing the answer
        System.out.println("Join Response:\n" +
                joinResponse.getId() + " " +
                joinResponse.getIpAddress() + " " +
                joinResponse.getPort() + " " +
                joinResponse.getMessage());


        // TODO: closing the channel when the node want to esc
        channel.shutdown();

    }

    public void createTokenData(){

        final ManagedChannel channel = ManagedChannelBuilder
                .forTarget(TargetNode.getInstance().getTargetIpAddress() + ":" + TargetNode.getInstance().getTargetPort())
                .usePlaintext(true).build();

        // creating a blocking stub on the channel
        NodeServiceBlockingStub stub = NodeServiceGrpc.newBlockingStub(channel);

        TokenData tokenData = TokenData.newBuilder().build();

        // send the token to the target node
        stub.tokenDelivery(tokenData);

        channel.shutdown();
    }

    public void addTokenData(double value){

        final ManagedChannel channel = ManagedChannelBuilder
                .forTarget(TargetNode.getInstance().getTargetIpAddress() + ":" + TargetNode.getInstance().getTargetPort())
                .usePlaintext(true).build();

        // creating a blocking stub on the channel
        NodeServiceBlockingStub stub = NodeServiceGrpc.newBlockingStub(channel);

        // adding the id and the value to the ready list
        TokenData tokenData = TokenData.newBuilder()
                .addReadyList(TokenData.ReadyList.newBuilder()
                        .setId(node.getId())
                        .setValue(value)
                        .build())
                .build();

        // send the token to the target node
        stub.tokenDelivery(tokenData);

        channel.shutdown();
    }

    public void skipTokenData(){

        final ManagedChannel channel = ManagedChannelBuilder
                .forTarget(TargetNode.getInstance().getTargetIpAddress() + ":" + TargetNode.getInstance().getTargetPort())
                .usePlaintext(true).build();

        // creating a blocking stub on the channel
        NodeServiceBlockingStub stub = NodeServiceGrpc.newBlockingStub(channel);

        // adding the id to the waiting list
        TokenData tokenData = TokenData.newBuilder()
                .addWaitingList(TokenData.WaitingList.newBuilder()
                        .setId(node.getId())
                        .build())
                .build();

        // send the token to the target node
        stub.tokenDelivery(tokenData);

        channel.shutdown();
    }


}
