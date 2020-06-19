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
            // createTokenData();
        } else {
            addToken();
        }



    }


    public void syncJoinNet() {

        ManagedChannel channel = ManagedChannelBuilder
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


        channel.shutdown();

    }


    public void addToken(){

        ManagedChannel channel = ManagedChannelBuilder
                .forTarget(
                        TargetNode.getInstance().getTargetIpAddress() + ":" +
                        TargetNode.getInstance().getTargetPort())
                .usePlaintext(true).build();

        // creating a blocking stub on the channel
        NodeServiceBlockingStub stub = NodeServiceGrpc.newBlockingStub(channel);

        /* DA DECOMMENTARE
        // adding the id and the value to the ready list
        TokenData tokenData = TokenData.newBuilder()
                .addReadyList(TokenData.ReadyList.newBuilder()
                        .setId(node.getId())
                        .setValue(LocalAvgList.getInstance().getLastValue())
                        .build())
                .build();
        */

        TokenData tokenData = TokenData.newBuilder().build();

        // send the token to the target node
        stub.tokenDelivery(tokenData);

        channel.shutdown();
    }


}
